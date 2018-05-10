package com.yodean.oa.common.exception;

import com.yodean.oa.common.enums.ResultCode;

/**
 * Created by rick on 2018/3/15.
 */
public class OAException extends RuntimeException {
    private Integer code;

    private String msg;

    private Exception exception;

    public OAException(ResultCode resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
    }

    public OAException(ResultCode resultEnum, Exception exception) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
        this.exception = exception;
    }

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

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
