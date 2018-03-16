package com.yodean.oa.common.handle;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by rick on 2018/3/15.
 */
@ControllerAdvice
public class ExceptionHandle {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        logger.error("【发生异常】{}", e);

        if (e instanceof  OAException) {
            OAException ex = (OAException)e;
            return ResultUtil.error(ex.getCode(), ex.getMessage());
        }

        return ResultUtil.error(e.getMessage());

    }
}
