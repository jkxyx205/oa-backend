package com.yodean.oa.common.aspect;

import com.yodean.oa.common.entity.DataEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by rick on 2018/3/19.
 */
//@Aspect
//@Component
public class JpaSaveAspect {

//    @Pointcut("execution(public * com.yodean.oa..service.*Service.save(..))")
    public void save() {

    }

//    @Before("save()")
    public void beforeSave(JoinPoint joinPoint) {
        if (ArrayUtils.isNotEmpty(joinPoint.getArgs())) {
            Object obj = joinPoint.getArgs()[0];
            if (obj instanceof DataEntity) {
                DataEntity entity = (DataEntity) obj;
                Date now = new Date();
                String curUser = "admin";
                if (entity.getId() == null) {
                    entity.setCreateBy(curUser);
                    entity.setCreateDate(now);
                    entity.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
                }

                entity.setUpdateBy(curUser);
                entity.setUpdateDate(now);
            }
        }
    }
}
