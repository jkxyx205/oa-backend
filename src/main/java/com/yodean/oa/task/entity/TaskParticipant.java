package com.yodean.oa.task.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "t_task_participant")
public class TaskParticipant extends DataEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
