package com.yodean.oa.sys.dictionary.core;

import com.yodean.oa.common.config.Global;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.sys.dictionary.entity.Word;
import com.yodean.oa.sys.dictionary.service.DictionaryService;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by rick on 5/17/18.
 */
public final class DictionaryUtils {
    private static final String PARAM_SEPARATOR = "#";

    private static Word parse(String category, String name) {
        DictionaryService dictionaryService = Global.applicationContext.getBean(DictionaryService.class);

        Word word = dictionaryService.findByCategoryAndName(category, name);
        return word;
    }

    public static void parse(Object obj) {
        if (Objects.isNull(obj)) return;

        if (obj instanceof Iterable) {
            Iterable iterable = (Iterable)obj;
            Iterator it =  iterable.iterator();
            while(it.hasNext()) {
                parse(it.next());
            }

        } else if(obj instanceof Map) {
            Map map = (Map)obj;

            Set<Map.Entry> set = map.entrySet();

            for(Map.Entry en : set) {
                parse(en.getKey());
                parse(en.getValue());
            }

        } else if(!isMappingJavaType(obj.getClass()) &&
                obj.getClass() != ClassLoader.class &&
                obj.getClass() != Class.class) {

            PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
            PropertyDescriptor[] propertyDescriptors = propertyUtilsBean.getPropertyDescriptors(obj.getClass());

            for (PropertyDescriptor p : propertyDescriptors) {
                try {
                    String propertyName = p.getName();
                    Object value =  propertyUtilsBean.getProperty(obj, propertyName);
                    if (p.getPropertyType() == Word.class) { //符合转换条件
                        Word _word = (Word)value;
                        PropertyUtils.setProperty(obj, p.getName(), parse(_word.getCategory(), _word.getName()));
                    } else {
                        parse(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new OAException(ResultCode.SERVER_ERROR, e);
                }

            }

        }

    }

    private static boolean isMappingJavaType(Class<?> type) {
        return type == Boolean.class || type == Character.class || type == Byte.class || type == Short.class
                || type == Integer.class || type == Long.class || type == Float.class || type == Double.class
                || type == Date.class || type == java.sql.Date.class || type == String.class || type == BigInteger.class || type == BigDecimal.class
                || type == Byte[].class || type == Blob.class || type == Clob.class || type == Timestamp.class
                || type.isEnum();
    }

    static Word string2Word(String s) {
        if (StringUtils.isBlank(s)) return null;

        String[] values = s.split(PARAM_SEPARATOR);

        Word word = new Word();
        word.setCategory(values[0]);
        word.setName(values[1]);
        return word;
    }
}
