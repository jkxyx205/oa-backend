package com.yodean.oa.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by rick on 2018/3/15.
 */
@Component
@ConfigurationProperties(prefix = "configuration")
public class Configuration {

    private String cdn;

    private String cdn2;

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public String getCdn2() {
        return cdn2;
    }

    public void setCdn2(String cdn2) {
        this.cdn2 = cdn2;
    }
}
