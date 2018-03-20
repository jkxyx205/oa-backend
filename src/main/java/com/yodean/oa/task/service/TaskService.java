package com.yodean.oa.task.service;

import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.common.exception.OANoSuchElementException;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.label.entity.LabelRelationShip;
import com.yodean.oa.sys.label.service.LabelRelationShipService;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.task.dao.TaskRepository;
import com.yodean.oa.task.entity.Task;
import com.yodean.oa.task.entity.TaskParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private TaskParticipantService taskParticipantService;

    @Resource
    private LabelRelationShipService labelRelationShipService;

    @Resource
    private BaseService baseService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Task save(Task task) {

        List<User> participants = task.getParticipants();
        List<Label> labels = task.getLabels();

        task = taskRepository.save(task);

        //add Participants
        deleteAllParticipantsByTaskId(task.getId());

        for (User user : participants) {
            TaskParticipant taskParticipant = new TaskParticipant();
            taskParticipant.setTaskId(task.getId());
            taskParticipant.setUserId(user.getId());
            taskParticipantService.save(taskParticipant);
        }

        //add labels
        labelRelationShipService.deleteLabel(CategoryEnum.TASK, task.getId());

        for (Label label : labels) {
            LabelRelationShip labelRelationShip = new LabelRelationShip();
            labelRelationShip.setLabelId(label.getId());
            labelRelationShip.setCategory(CategoryEnum.TASK);
            labelRelationShip.setCategoryId(task.getId());
            labelRelationShipService.save(labelRelationShip);
        }

        task.setLabels(labels);
        task.setParticipants(participants);

        logger.info("saved task【{}】,detail is {}",task.getTitle(), task);
        return task;
    }

    public void deleteAllParticipantsByTaskId(Integer id) {
        String sql = "DELETE FROM t_task_participant where task_id = ?";
        jdbcTemplate.update(sql, new Object[] {id});
    }


    public Task findById(Integer id) {
        Optional<Task> optional = taskRepository.findById(id);

        if (optional.isPresent()) {
            Task task = optional.get();
            task.setParticipants(getParticipantsByTaskId(id));
            task.setLabels(labelRelationShipService.getLabels(CategoryEnum.TASK, id));
            return task;
        }

        throw new OANoSuchElementException();
    }

    private List<User> getParticipantsByTaskId(Integer id) {
        String sql = "SELECT\n" +
                "\tid, name, remarks, update_by updateBy, update_Date updateDate,create_by createBy, create_date createDate\n" +
                "FROM\n" +
                "\tsys_user u\n" +
                "WHERE\n" +
                "\tEXISTS (\n" +
                "\t\tSELECT\n" +
                "\t\t\t1\n" +
                "\t\tFROM\n" +
                "\t\t\tt_task_participant tp\n" +
                "\t\tWHERE\n" +
                "\t\t\ttp.task_id = :id\n" +
                "\t\tAND u.id = tp.user_id)";

        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        return baseService.query(sql, params, User.class);
    }

}
