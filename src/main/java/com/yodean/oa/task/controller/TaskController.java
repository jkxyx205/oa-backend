package com.yodean.oa.task.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultEnum;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.task.entity.Task;
import com.yodean.oa.task.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by rick on 2018/3/19.
 */
@RestController
@RequestMapping("/tasks")
@Api(description = "任务模块")
public class TaskController {

    @Resource
    private TaskService taskService;


    @PostMapping("")
    @ApiOperation(value = "新建任务")
    public Result<Task> save(@Valid @RequestBody Task task, BindingResult result) {
        if (result.hasErrors()) {
            return ResultUtil.error(ResultEnum.VALIDATE_ERROR, result.getAllErrors());
        }

        taskService.save(task);
        //TODO

        return ResultUtil.success(task);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取任务")
    public Result<Task> findById(@PathVariable Integer id) {
        Task task = taskService.findById(id);
        return ResultUtil.success(task);
    }
}
