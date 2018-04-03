package com.yodean.oa.common.util;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultType;

/**
 * Created by rick on 2018/3/15.
 */
public final class ResultUtil {

    public static Result success() {
        return success(null);
    }

    public static Result success(Object data) {
        Result<Object> result = new Result();
        result.setData(data);
        result.setCode(ResultType.SUCCESS.getCode());
        result.setMsg(ResultType.SUCCESS.getMessage());
        return result;
    }

    public static Result error(String msg) {
        return error(ResultType.UNKNOW_ERROR.getCode(), msg);
    }

    public static Result error(Integer code, String msg) {
        return error(code, msg, null);
    }

    public static Result error(ResultType resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public static Result error(ResultType resultEnum, Object data) {
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
