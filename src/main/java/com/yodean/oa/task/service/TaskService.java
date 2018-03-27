package com.yodean.oa.task.service;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
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
    private TaskRepository taskRepository;

    @Resource
    private LabelService labelService;

    @Resource
    private WorkspaceService workspaceService;

    @Transactional
    public Task save(Task task) {

        task.setDelFlag(DataEntity.DEL_FLAG_NORMAL);

        Task persit = taskRepository.save(task);
        task.setId(persit.getId());

        workspaceService.save(CategoryEnum.TASK, task.getId(), task.getUserIds());

        //add Label
        labelService.save(CategoryEnum.TASK, task.getId(), task.getLabels());

        logger.info("saved task【{}】,detail is {}",task.getTitle(), task);
        return task;
    }


    /***
     * load all
     * @param id
     * @return
     */
    public Task findById(Integer id) {
       return findById(id, false);
    }

    public Task findById(Integer id, boolean lazy) {
        Optional<Task> optional = taskRepository.findById(id);

        if (optional.isPresent()) {
            Task task = optional.get();
            if (!lazy) {
                task.setUsers(workspaceService.findUsers(CategoryEnum.TASK, id));
                task.setLabels(labelService.findLabels(CategoryEnum.TASK, id));
            }
            return task;
        }

        throw new OANoSuchElementException();
    }


    /***
     * 本地删除任务
     * @param taskId
     */
    public void trash(Integer taskId) {
        workspaceService.move(CategoryEnum.TASK, taskId, CategoryStatus.TRASH);
    }

    public void move2Inbox(Integer taskId) {
        workspaceService.move(CategoryEnum.TASK, taskId, CategoryStatus.INBOX);
    }

    public void archive(Integer taskId) {
        workspaceService.move(CategoryEnum.TASK, taskId, CategoryStatus.ARCHIVE);
    }

    public void delete(Integer taskId) {
        workspaceService.move(CategoryEnum.TASK, taskId, CategoryStatus.DELETE);
    }
}
