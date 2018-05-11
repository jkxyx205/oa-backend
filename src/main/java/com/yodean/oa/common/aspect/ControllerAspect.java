package com.yodean.oa.common.aspect;

import com.yodean.oa.sys.util.UserUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rick on 2018/3/15.
 * 记录请求信息
 */
@Aspect
@Component
public class ControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(public * com.yodean..*Controller.*(..))")
    public void log() {

    }

    @Before("log()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Before => logAspect => {},{}" , UserUtils.getUser().getId(), UserUtils.getUser().getChineseName());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("url={}", request.getRequestURL());

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //class method
        logger.info("class-method={}", joinPoint.getSignature().getDeclaringTypeName() +"." +  joinPoint.getSignature().getName());

        //params
        logger.info("class-params={}", joinPoint.getArgs());

    }

    @After("log()")
    public void logAfter() {
//        logger.info("After => logAspect");
    }

    @AfterReturning(returning="obj",pointcut = "log()")
    public void afterReturn(Object obj) {
        logger.info("response={}", obj);
    }
}