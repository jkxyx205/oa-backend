package com.yodean.oa.task.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.util.ResultUtils;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import com.yodean.oa.task.entity.Discussion;
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

    @Resource
    private WorkspaceService workspaceService;


    @PostMapping
    public Result<Integer> save(@Valid @RequestBody Task task, BindingResult result) {
        if (result.hasErrors()) {
            return ResultUtils.error(ResultCode.VALIDATE_ERROR, result.getAllErrors());
        }

        return ResultUtils.success(taskService.save(task));
    }

    @PutMapping("/{id}")
    public Result<Integer> update(@RequestBody Task task, @PathVariable Integer id) {
        taskService.update(task, id);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public Result<Task> findById(@PathVariable Integer id) {
        Task task = taskService.findById(id);
        return ResultUtils.success(task);
    }

    /***
     * 添加讨论
     * @return
     */
    @PostMapping("{id}/discussions")
    public Result addDiscussion(@PathVariable Integer id, @RequestBody Discussion discussion) {
        taskService.addDiscussion(id, discussion);
        return ResultUtils.success();
    }

    /***
     * 删除讨论
     * @return
     */
    @DeleteMapping("discussions/{id}")
    public Result deleteDiscussion(@PathVariable Integer id) {
        taskService.deleteDiscussion(id);
        return ResultUtils.success();
    }

}
