package com.yodean.oa.common.job.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.job.SampleJob;
import com.yodean.oa.common.job.dto.ScheduleDetail;
import com.yodean.oa.common.job.service.ScheduleService;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.user.service.UserService;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/23.
 */
@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping
    public Result<ScheduleDetail> add(ScheduleDetail schedule) throws SchedulerException {

        schedule.setJob(SampleJob.class);
        schedule.getJobProps().put("name", "Rick");

        scheduleService.addJob(schedule);

        return ResultUtil.success(schedule);
    }

    @PutMapping
    public Result<ScheduleDetail> update(@RequestBody ScheduleDetail schedule) throws SchedulerException {

        schedule.setJob(SampleJob.class);
        schedule.getJobProps().put("name", "Ashley");

        scheduleService.updateJob(schedule);
        return ResultUtil.success(schedule);
    }

    @DeleteMapping("/{name}")
    public Result delete(@PathVariable String name) throws SchedulerException {
        scheduleService.deleteJob(name, CategoryEnum.TASK.name());
        return ResultUtil.success();
    }
}
