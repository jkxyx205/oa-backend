package com.yodean.oa.task.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultEnum;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.task.entity.Task;
import com.yodean.oa.task.service.TaskService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by rick on 2018/3/19.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Resource
    private TaskService taskService;


    @PostMapping
    public Result<Task> save(@Valid @RequestBody Task task, BindingResult result) {
        if (result.hasErrors()) {
            return ResultUtil.error(ResultEnum.VALIDATE_ERROR, result.getAllErrors());
        }

        taskService.save(task);
        //TODO

        return ResultUtil.success(task);
    }

    @GetMapping("/{id}")
    public Result<Task> findById(@PathVariable Integer id) {
        Task task = taskService.findById(id, false);
        return ResultUtil.success(task);
    }

    @PostMapping("/{id}/trash")
    public Result<String> trash(@PathVariable Integer id) {
        taskService.trash(id);
        return ResultUtil.success();
    }

    @PostMapping("/{id}/inbox")
    public Result<String> move2Inbox(@PathVariable Integer id) {
        taskService.move2Inbox(id);
        return ResultUtil.success();
    }
}
