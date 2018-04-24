package com.yodean.oa.common.handler;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rick on 2018/3/15.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Object handler(HttpServletRequest request, Exception e) {
        logger.error("发生异常", e);

        Result result;

        if (e instanceof  OAException) {
            OAException ex = (OAException)e;
            result = ResultUtil.error(ex.getCode(), ex.getMessage());
         } else if (e instanceof MaxUploadSizeExceededException) {
            result = ResultUtil.error(ResultCode.VALIDATE_ERROR, "文件不能超过5M");
        }else {
            result = ResultUtil.error(e.getMessage());
        }

//        if (isAjaxRequest(request)) {
            return result;
//        } else {
//            ModelAndView mv = new ModelAndView();
//            mv.addObject("result", result);s
//            mv.setViewName("/common/error");
//            return mv;
//        }
    }

    /**
     * isAjaxRequest:判断请求是否为Ajax请求. <br/>
     *
     * @param request 请求对象
     * @return boolean
     * @since JDK 1.6
     */
    public boolean isAjaxRequest(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true:false;
        return isAjax;
    }
}
