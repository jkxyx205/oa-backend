package com.yodean.oa.task.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by rick on 2018/3/19.
 */
@Entity(name = "t_task_participant")
public class TaskParticipant extends DataEntity {

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
