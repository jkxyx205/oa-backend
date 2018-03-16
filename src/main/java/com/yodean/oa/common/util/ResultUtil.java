package com.yodean.oa.common.util;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultEnum;

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
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMessage());
        return result;
    }

    public static Result error(String msg) {
        return error(ResultEnum.UNKNOW_ERROR.getCode(), msg);
    }

    public static Result error(Integer code, String msg) {
        Result<Object> result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
