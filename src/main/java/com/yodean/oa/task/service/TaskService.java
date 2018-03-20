package com.yodean.oa.task.service;

import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.task.dao.TaskRepository;
import com.yodean.oa.task.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by rick on 2018/3/19.
 */

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private BaseService baseService;

    public Task save(Task task) {
        task = taskRepository.save(task);
        logger.info("saved task【{}】,detail is {}",task.getTitle(), task);
        return task;
    }

    public Task findById(Integer id) {
        Optional<Task> optional = taskRepository.findById(id);

        if (optional.isPresent())
            return optional.get();

        throw new OANoSuchElementException();
    }


}
