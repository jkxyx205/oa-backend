package com.yodean.oa.common.util;

import com.yodean.oa.common.entity.DataEntity;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
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

    /**
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
        merge(src, obj, deep, (src1, propertyName, srcValue, objValue) -> Objects.nonNull(objValue));
    }

    private static void merge(Object src, Object obj, boolean deep, SetValueAble setValueable) {
        if (Objects.isNull(src) || Objects.isNull(obj)) {
            return;
        }

        PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] propertyDescriptorsOfSrc = propertyUtilsBean.getPropertyDescriptors(src.getClass());

        PropertyDescriptor[] propertyDescriptorsOfObj = propertyUtilsBean.getPropertyDescriptors(obj.getClass());

        Set<String> propertyNames = new HashSet<>(propertyDescriptorsOfObj.length);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorsOfObj) {
            propertyNames.add(propertyDescriptor.getName());
        }

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
                            }

                        }

                        Iterator<Object> iterator = srcCollection.iterator();
                        while (iterator.hasNext()) {
                            Object objSrc = iterator.next();
                            if (!objCollection.contains(objSrc)) {
                                iterator.remove();
                            }
                        }
                    } else if (DataEntity.class.isAssignableFrom(type)){// Entity Object
                        merge(srcPropertyValue, objPropertyValue, deep, setValueable);
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
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
}