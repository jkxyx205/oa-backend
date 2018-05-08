package com.yodean.oa.task.service;

import com.yodean.oa.common.enums.Category;
import com.yodean.oa.common.enums.DocumentCategory;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.common.plugin.document.service.DocumentService;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import com.yodean.oa.sys.workspace.entity.Workspace;
import com.yodean.oa.sys.workspace.service.WorkspaceService;
import com.yodean.oa.task.dao.DiscussionRepository;
import com.yodean.oa.task.dao.TaskLogRepository;
import com.yodean.oa.task.dao.TaskRepository;
import com.yodean.oa.task.entity.Discussion;
import com.yodean.oa.task.entity.Task;
import com.yodean.oa.task.entity.TaskLog;
import com.yodean.oa.task.enums.TaskLogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.Doc;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by rick on 2018/3/19.
 */

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Resource
    private TaskLogRepository taskLogRepository;

    @Resource
    private DiscussionRepository discussionRepository;

    @Resource
    private DocumentService documentService;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private WorkspaceService workspaceService;

    @Resource
    private UserService userService;

    @Transactional
    public Integer save(Task task) {
        build(task);

        taskRepository.save(task);

        //add document
//        documentService.update(task.getDocIds(), DocumentCategory.TASK, task.getId());

        //add log
//        addTaskLog(task, true);

       return task.getId();
    }

    private void build(Task task) {
        List<Label> labels = task.getLabels();
        labels.forEach(label -> label.setLabelId(new Label.LabelId(Label.LabelCategory.TASK, task.getId())));

        List<Workspace> workspaces =  task.getWorkspaces();
        workspaces.forEach(workspace -> workspace.setCategory(Category.TASK));

        //
        task.getDocIds().forEach(docId -> {
            Document document = documentService.findById(docId);
            document.setCategory(DocumentCategory.TASK);
            task.getDocuments().add(document);
        });

    }

    @Transactional
    public void update(Task task, Integer id) {
        task.setId(id);
        build(task);

        taskRepository.updateCascade(task);

        //add document
//        documentService.update(task.getDocIds(), DocumentCategory.TASK, task.getId());

        workspaceService.tip(Category.TASK, task.getId());

        //add log
//        addTaskLog(task, false);
    }



    public Task findById(Integer id) {
        Optional<Task> optional = taskRepository.findById(id);

        if (optional.isPresent()) {
            Task task = optional.get();
//            task.setUsers(workspaceService.findUsers(Category.TASK, id));
//            task.setLabels(labelService.findLabels(Label.LabelCategory.TASK, id));
//            task.setDocuments(documentService.findById(DocumentCategory.TASK, id));

            //get Discussion document
            for (Discussion discussion : task.getDiscussions()) {
                discussion.setDocuments(documentService.findById(DocumentCategory.TASK_DISCUSSION, discussion.getId()));
            }

            return task;
        }

        throw new OANoSuchElementException();
    }



    private void addTaskLog(Task task, boolean isSave) {

        TaskLog taskLog = new TaskLog();
        taskLog.setTask(task);

        if (isSave) {
           taskLog.setTaskLogType(TaskLogType.TASK);
        } else if (null != task.getTitle()){
            taskLog.setTaskLogType(TaskLogType.TITLE)
                    .setDetail(task.getTitle());
        } else if (null != task.getPriority()){
            taskLog.setTaskLogType(TaskLogType.PRIORITY, task.getPriority().getTitle());
        } else if (null != task.getContent()){
            taskLog.setTaskLogType(TaskLogType.CONTENT)
                    .setDetail(task.getContent());
        } else if (null != task.getProgress()){
            taskLog.setTaskLogType(TaskLogType.PROGRESS, task.getProgress() + "");
        }

        //TODO add User or remove member, use specific api

        taskLogRepository.save(taskLog);
    }

    @Transactional
    public void addUser(Integer id, Integer userId) {
//        workspaceService.tipUsers(Category.TASK, id, userId);
        //addLog
        addUserLog(id, userId, TaskLogType.USER_ADD);

    }

    @Transactional
    public void removeUser(Integer id, Integer userId) {
        workspaceService.remove(Category.TASK, id, userId);

        //addLog
        addUserLog(id, userId, TaskLogType.USER_REMOVE);
    }

    private void addUserLog(Integer id, Integer userId, TaskLogType taskLogType) {
        //addLog
        Task task = new Task();
        task.setId(id);
        User user = userService.findById(userId);
        TaskLog taskLog = new TaskLog(taskLogType, user.getChineseName());
        taskLog.setTask(task);
        taskLogRepository.save(taskLog);
    }


    public void delete(Integer id) {
        taskRepository.deleteLogical(id);
    }

    public void addDiscussion(Integer id, Discussion discussion) {
        Task task = findById(id);
        discussion.setTask(task);

        discussionRepository.save(discussion);

        documentService.update(discussion.getDocIds(), DocumentCategory.TASK_DISCUSSION, discussion.getId());
    }
}
