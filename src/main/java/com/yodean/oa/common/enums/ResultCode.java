package com.yodean.oa.common.enums;

/**
 * Created by rick on 2018/3/15.
 */
public enum ResultCode {
    UNKNOW_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    BAD_REQUEST_ERROR(400, "请求出现错误"),
    CERTIFIED_ERROR(401, "没有提供认证信息"),
    AUTHORITY_ERROR(403, "请求的资源不允许访问"),
    NOT_FOUND_ERROR(404, "请求的内容不存在"),
    METHOD_REQUEST_ERROR(405, "请求的方法不允许使用"),
    SERVER_ERROR(500, "服务器错误"),


    VALIDATE_ERROR(50001, "验证错误"),
    TOKEN_ERROR(50002, "拦截token出错"),
    EXISTS_ERROR(60000, "纪录已经存在"),
    NULL_ERROR(40001, "不合法的参数ID"),
    FILE_NAME_DUPLICATE_ERROR(40003, "文件名不能重复");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
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
