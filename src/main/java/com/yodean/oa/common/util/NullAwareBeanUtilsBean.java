package com.yodean.oa.common.util;

/**
 * Created by rick on 3/29/18.
 */

import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    private static final ContextClassLoaderLocal<NullAwareBeanUtilsBean> BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
        protected BeanUtilsBean initialValue() {
            return new NullAwareBeanUtilsBean();
        }
    };

    public static BeanUtilsBean getInstance() {
        return BEANS_BY_CLASSLOADER.get();
    }


    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value == null) return;

        try {
            Object obj = super.getPropertyUtils().getProperty(dest,name);

            if (Objects.nonNull(obj) && obj instanceof Collection) {//不用改变引用
                ((Collection) obj).addAll((Collection) value);
            } else {
                super.copyProperty(dest, name, value);
            }

        } catch (NoSuchMethodException e) {
            throw new OAException(ResultCode.UNKNOW_ERROR);
        }
    }

}
