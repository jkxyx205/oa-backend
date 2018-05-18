package com.yodean.oa.common.util;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import javax.persistence.Convert;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by rick on 5/8/18.
 */
public class EntityBeanUtils {
    private static final transient Logger logger = LoggerFactory.getLogger(EntityBeanUtils.class);

    /**
     * 对象是POJO，不能是集合对象
     * 合并对象属性，将obj的非null值合并到src，
     * 合并基本数据类型+ String + 枚举 + Date
     *
     * @param src
     * @param obj
     */
    public static void merge(Object src, Object obj) {
        merge(src, obj, true);
    }

    public static void merge(Object src, Object obj, boolean deep) {

        logger.info("Begin merge entity:{}", src.getClass());

        merge(src, obj, deep, (src1, propertyName, srcValue, objValue) -> {
            if (Objects.nonNull(objValue) && isNotEqual(srcValue, objValue)) {
                //if Date

                logger.info("Modify entity [{}] property [{}] from [{}] to [{}]", src1.getClass(), propertyName, srcValue, objValue);
                return true;
            }
            return  false;
        });

        logger.info("End merge entity:{}", src.getClass());
    }

    private static void merge(Object src, Object obj, boolean deep, SetValueAble setValueable) {
        if (Objects.isNull(src) || Objects.isNull(obj)) {
            return;
        }

        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] propertyDescriptorsOfSrc = propertyUtilsBean.getPropertyDescriptors(src.getClass());

        Set<String> propertyNames = setterNames(src.getClass()); //new HashSet<>(propertyDescriptorsOfObj.length);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptorsOfSrc) {
            String name = propertyDescriptor.getName();

            if (!propertyNames.contains(name)) continue;

            Class<?> type = propertyDescriptor.getPropertyType();




            try {
                Object srcPropertyValue = propertyUtilsBean.getProperty(src, name);
                Object objPropertyValue = propertyUtilsBean.getProperty(obj, name);

                if (srcPropertyValue == null) {
                    PropertyUtils.setProperty(src, name, objPropertyValue);
                    continue;
                }

                if (objPropertyValue == null)
                    continue;

                if (isMappingJavaType(type)) {
                    if (setValueable.beforeSetProperty(src, name, srcPropertyValue, objPropertyValue)) {
                        PropertyUtils.setProperty(src, name, objPropertyValue);
                    }

                } else if(isConverter(src.getClass(), name)) { //hibernate自定义属性

                    merge(srcPropertyValue, objPropertyValue, deep, setValueable);

                }  else if (deep) {

                    if (Collection.class.isAssignableFrom(type)) { //Collection
                        Collection srcCollection = (Collection)srcPropertyValue;
                        Collection objCollection = (Collection)objPropertyValue;

                        Map<Integer, Object> srcMapping = new HashMap<>(srcCollection.size());

                        for (Object objSrc : srcCollection) {
                            srcMapping.put(((DataEntity)(objSrc)).getId(), objSrc);
                        }

                        for (Object objSub : objCollection) {
                            if (srcCollection.contains(objSub)) { //存在合并

                                merge(srcMapping.get(((DataEntity)(objSub)).getId()), objSub, deep, setValueable);

                            }  else {//不存在直接添加
                                srcCollection.add(objSub);
                                logger.info("Add entity [{}] property [{}] with [{}]", src.getClass(), name, objSub);
                            }

                        }

                        Iterator<Object> iterator = srcCollection.iterator();
                        while (iterator.hasNext()) {
                            Object objSrc = iterator.next();
                            if (!objCollection.contains(objSrc)) {
                                iterator.remove();
                                logger.info("Remove entity [{}] property [{}] with [{}]", src.getClass(), name, objSrc);
                            }
                        }
                    } else if (DataEntity.class.isAssignableFrom(type)){// Entity Object
                        merge(srcPropertyValue, objPropertyValue, deep, setValueable);
                    }
                }

            } catch (Exception e) {
                throw new OAException(ResultCode.SERVER_ERROR, e);
            }

        }
    }

    private static boolean isMappingJavaType(Class<?> type) {
        return type == Boolean.class || type == Character.class || type == Byte.class || type == Short.class
                || type == Integer.class || type == Long.class || type == Float.class || type == Double.class
                || type == Date.class || type == String.class || type == BigInteger.class || type == BigDecimal.class
                || type == Byte[].class || type == Blob.class || type == Clob.class || type == Timestamp.class
                || type.isEnum();
    }

    private interface SetValueAble {
        boolean beforeSetProperty(Object src, String propertyName, Object srcValue, Object objValue);
    }


    private static Set<String> setterNames(Class<?> c) {
        Set<String> set = new HashSet<>();

        Method[] methods = c.getMethods();
        for (Method method : methods) {
            if (isSetter(method)) {
                set.add(StringUtils.unCapitalize(method.getName().substring(3)));
            }
        }

        return set;
    }

    private static boolean isNotEqual(Object obj1, Object obj2) {
        if (Objects.equals(obj1, obj2)) {
            return false;
        }

        if (obj1 instanceof Date && obj2 instanceof Date) {
            Date date1 = (Date)obj1;
            Date date2 = (Date)obj2;

            return date1.getTime() != date2.getTime();
        }

        return true;

    }

    private static boolean isConverter(Class<?> aClass, String name) throws NoSuchFieldException {
        Field field = aClass.getDeclaredField(name);

        return field.isAnnotationPresent(Convert.class);
    }

    public static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) return false;
        if(method.getParameterTypes().length != 1) return false;
        return true;
    }
}