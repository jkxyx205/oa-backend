package com.yodean.oa.common.exception;

import com.yodean.oa.common.enums.ResultEnum;

/**
 * Created by rick on 2018/3/15.
 */
public class OAException extends RuntimeException {
    private Integer code;

    private String msg;

    public OAException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMessage();
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
}
