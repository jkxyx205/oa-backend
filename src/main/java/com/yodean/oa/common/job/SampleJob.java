package com.yodean.oa.common.job;

import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rick on 2018/3/23.
 */
@Service
@DisallowConcurrentExecution
public class SampleJob extends QuartzJobBean {

    private String name;

    @Resource
    private UserService userService;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
//        UserService userService = Global.applicationContext.getBean(UserService.class);
        User user = userService.findById(1);
        System.out.println(String.format("Hello %s! time is %s", this.name,
                new SimpleDateFormat("HH:mm:ss").format(new Date())) + " -> " + user.getChineseName());
    }

}
