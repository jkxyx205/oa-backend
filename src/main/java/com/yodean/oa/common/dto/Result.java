package com.yodean.oa.common.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by rick on 2018/3/15.
 */
public class Result<T> {
    private Integer code;

    private String msg;

    private T data;

    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
