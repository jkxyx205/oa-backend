package com.yodean.oa.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by rick on 2018/3/22.
 */
@Component
@ConfigurationProperties("configuration")
public class Global {

    public static String DOCUMENT;

    public static String CDN;

    public void setDocument(String document) {
        this.DOCUMENT = document;
    }

    public void setCdn(String cdn) {
        this.CDN = cdn;
    }
}
