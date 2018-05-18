package com.yodean.oa.common.util;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;

/**
 * Created by rick on 2018/3/15.
 */
public final class ResultUtil {

    public static Result success() {
        return success(null);
    }

    public static <T> Result success(T data) {
        Result<T> result = new Result();
        result.setData(data);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMessage());
        return result;
    }

    public static Result error(String msg) {
        return error(ResultCode.UNKNOW_ERROR.getCode(), msg);
    }

    public static Result error(Integer code, String msg) {
        return error(code, msg, null);
    }

    public static Result error(ResultCode resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public static Result error(ResultCode resultEnum, Object data) {
        return error(resultEnum.getCode(), resultEnum.getMessage(), data);
    }

    public static Result error(Integer code, String msg, Object data) {
        Result<Object> result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
