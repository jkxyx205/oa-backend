package com.yodean.oa.meeting.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.task.enums.Priority;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 2018/3/19.
 */
@Entity(name = "t_meeting")
@DynamicUpdate
public class Meeting extends DataEntity {
    /***
     * 会议标题
     */
    @NotBlank(message = "名称不能为空")
    @Column(nullable = false)
    private String title;

    @Length(max = 10000, message = "正文内容不能超过10000个字符")
    @NotBlank(message = "会议正文不能为空")
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    /***
     * 会议开始时间
     */
    @Column(name="start_date")
    private Date startDate;

    /***
     * 会议结束时间
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

    @Transient
    private List<Label> labels;

    /***
     * 必须参加
     */
    @Transient
    private List<Integer> toUserIds;

    /***
     * 可选
     */
    @Transient
    private List<Integer> ccUserIds;

    @Transient
    private List<User> toUsers;

    @Transient
    private List<User> ccUsers;

    private Integer[] test;

    public Integer[] getTest() {
        return test;
    }

    public void setTest(Integer[] test) {
        this.test = test;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Integer> getToUserIds() {
        return toUserIds;
    }

    public void setToUserIds(List<Integer> toUserIds) {
        this.toUserIds = toUserIds;
    }

    public List<Integer> getCcUserIds() {
        return ccUserIds;
    }

    public void setCcUserIds(List<Integer> ccUserIds) {
        this.ccUserIds = ccUserIds;
    }

    public List<User> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<User> toUsers) {
        this.toUsers = toUsers;
    }

    public List<User> getCcUsers() {
        return ccUsers;
    }

    public void setCcUsers(List<User> ccUsers) {
        this.ccUsers = ccUsers;
    }
}