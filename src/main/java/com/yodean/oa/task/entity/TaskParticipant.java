package com.yodean.oa.task.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "t_task_participant")
public class TaskParticipant extends DataEntity {


    @Column(name = "user_id")
    private Integer userId;


    @Column(name = "task_id")
    private Integer taskId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
