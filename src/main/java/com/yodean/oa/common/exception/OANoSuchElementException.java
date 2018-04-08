package com.yodean.oa.common.exception;

import com.yodean.oa.common.enums.ResultCode;

/**
 * Created by rick on 2018/3/15.
 */
public class OANoSuchElementException extends OAException {

    public OANoSuchElementException() {
        super(ResultCode.NOT_FOUND_ERROR);
    }
}
