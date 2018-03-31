package com.yodean.oa.task.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.service.LabelService;
import com.yodean.oa.sys.util.UserUtils;
import com.yodean.oa.sys.workspace.enums.CategoryStatus;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.task.dao.TaskRepository;
import com.yodean.oa.task.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by rick on 2018/3/19.
 */

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Resource
    private DocumentService documentService;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private LabelService labelService;

    @Resource
    private WorkspaceService workspaceService;

    @Transactional
    public Integer save(Task task) {
        taskRepository.save(task);

        //add Label
        labelService.save(CategoryEnum.TASK, task.getId(), task.getLabels());

        workspaceService.tipUsers(CategoryEnum.TASK, task.getId(), task.getUserIds());

        logger.info("saved task【{}】,detail is {}",task.getTitle(), task);
        return task.getId();
    }



    public Task findById(Integer id) {
        Optional<Task> optional = taskRepository.findById(id);

        if (optional.isPresent()) {
            Task task = optional.get();
            task.setUsers(workspaceService.findUsers(CategoryEnum.TASK, id));
            task.setLabels(labelService.findLabels(CategoryEnum.TASK, id));
            task.setDocuments(documentService.findById(CategoryEnum.TASK, id));
            return task;
        }

        throw new OANoSuchElementException();
    }

    @Transactional
    public void update(Task task, Integer id) {
        task.setId(id);
        taskRepository.updateNonNull(task);
        workspaceService.tipAll(CategoryEnum.TASK, task.getId());
    }

    public void delete(Integer id) {
        taskRepository.deleteLogical(id);
    }
}
