package com.yodean.oa.common.util;

/**
 * Created by rick on 3/29/18.
 */
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    private static final ContextClassLoaderLocal<NullAwareBeanUtilsBean> BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
        protected BeanUtilsBean initialValue() {
            return new NullAwareBeanUtilsBean();
        }
    };

    public static BeanUtilsBean getInstance() {
        return (BeanUtilsBean)BEANS_BY_CLASSLOADER.get();
    }


    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value==null)return;
        super.copyProperty(dest, name, value);
    }

}
