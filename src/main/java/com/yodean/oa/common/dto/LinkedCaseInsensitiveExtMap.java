package com.yodean.oa.common.dto;


import com.yodean.oa.common.util.JacksonUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * Created by rick on 2017/7/27.
 */
public class LinkedCaseInsensitiveExtMap extends LinkedCaseInsensitiveMap {

    public LinkedCaseInsensitiveExtMap(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public String toString() {
        return JacksonUtils.toJSon(this);
    }
}
