package com.yodean.oa.common.aspect;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by rick on 2018/3/15.
 */
@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(public * com.yodean.oa..*Service.*(..))")
    public void log() {

    }

    @Before("log()")
    public void logBefore() {

    }

    @After("log()")
    public void logAfter(JoinPoint joinPoint) {
        logger.info("Begin Service method.....");
        //class method
        logger.info("class-method={}", joinPoint.getSignature().getDeclaringTypeName() +"." +  joinPoint.getSignature().getName());

        //params
        if (ArrayUtils.isNotEmpty(joinPoint.getArgs())) {
            for (Object o : joinPoint.getArgs()) {
                logger.info("method param:{}", o);
            }
        }
    }

    @AfterReturning(returning="obj",pointcut = "log()")
    public void afterReturn(Object obj) {
        logger.info("The return value is [{}]", obj);
        logger.info("End Service.....");
    }
}