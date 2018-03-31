package com.yodean.oa.task.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.task.enums.Priority;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 2018/3/19.
 */
@Entity(name = "t_task")
@DynamicUpdate
public class Task extends DataEntity {
    /***
     * 任务名称
     */
    @NotBlank(message = "名称不能为空")
    @Column(nullable = false)
    private String title;

    @Length(max = 10000, message = "正文内容不能超过10000个字符")
    @NotBlank(message = "任务正文不能为空")
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    /***
     * 任务开始时间
     */
    @Column(name="start_date")
    private Date startDate;

    /***
     * 任务结束时间
     */
    @Column(name="end_date")
    private Date endDate;

    /***
     * 提醒开始时间
     */
    @Column(name="tip_start_date")
    private Date tipStartDate;

    /***
     * 提醒结束时间
     */
    @Column(name="tip_end_date")
    private Date tipEndDate;

    /***
     * 优先级
     */
    @Enumerated(EnumType.STRING)
    @Column
    private Priority priority;

    /***
     * 参与人
     */
    @Transient
    private Set<Integer> userIds;

    @Transient
    private List<User> users;

    @Transient
    private List<Label> labels;

    @Transient
    private Set<Integer> docIds;

    @Transient
    private List<Document> documents;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getTipStartDate() {
        return tipStartDate;
    }

    public void setTipStartDate(Date tipStartDate) {
        this.tipStartDate = tipStartDate;
    }

    public Date getTipEndDate() {
        return tipEndDate;
    }

    public void setTipEndDate(Date tipEndDate) {
        this.tipEndDate = tipEndDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Set<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(Set<Integer> docIds) {
        this.docIds = docIds;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}