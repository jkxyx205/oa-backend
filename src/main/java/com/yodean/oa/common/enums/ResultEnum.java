package com.yodean.oa.common.enums;

/**
 * Created by rick on 2018/3/15.
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1, "未知错误"),
    NOT_FOUND_ERROR(404, "未找到相关纪录"),
    SUCCESS(200, "成功");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}