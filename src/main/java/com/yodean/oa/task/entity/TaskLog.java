package com.yodean.oa.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.task.enums.TaskLogType;

import javax.persistence.*;

/**
 * Created by rick on 4/2/18.
 */
@Entity
@Table(name = "sys_task_log")
@JsonIgnoreProperties("id")
public class TaskLog extends DataEntity {

    @Column(name = "log_type")
    @Enumerated(EnumType.STRING)
    private TaskLogType taskLogType;

    private String title;

    private String detail;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @JsonIgnore
    private Task task;

    public TaskLog() {}


    public TaskLog(TaskLogType taskLogType, String ... params) {
       setTaskLogType(taskLogType, params);
    }

    public TaskLogType getTaskLogType() {
        return taskLogType;
    }

    public TaskLog setTaskLogType(TaskLogType taskLogType, String ... params) {
        this.taskLogType = taskLogType;
        this.title = String.format(taskLogType.getLogName(), params);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskLog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public TaskLog setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}