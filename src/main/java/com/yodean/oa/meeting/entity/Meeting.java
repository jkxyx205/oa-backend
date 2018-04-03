package com.yodean.oa.meeting.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.plugin.document.entity.Document;
import com.yodean.oa.sys.enums.Priority;
import com.yodean.oa.sys.label.entity.Label;
import com.yodean.oa.sys.workspace.dto.Participant;
import com.yodean.oa.sys.workspace.entity.Workspace;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rick on 2018/3/19.
 */
@Entity(name = "t_meeting")
@DynamicUpdate
public class Meeting extends DataEntity {

    public enum MeetingType {
        MEETING, SCHEDULE;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type", nullable = false)
    private MeetingType meetingType;

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
     * 会议地址
     */
    private String address;

    /***
     * 可见度
     */
    private Boolean privacy;

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

    /***
     * 标签
     */
    @Transient
    private List<Label> labels;


    @Transient
    private Set<Integer> docIds;
    /***
     * 附件
     */
    @Transient
    private List<Document> documents;

    @Transient
    private Map<Integer, Workspace.UserType> userMap;

    /***
     * 参与人员
     */
    @Transient
    private List<Participant> users;

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

    public MeetingType getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
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

    public Map<Integer, Workspace.UserType> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<Integer, Workspace.UserType> userMap) {
        this.userMap = userMap;
    }

    public List<Participant> getUsers() {
        return users;
    }

    public void setUsers(List<Participant> users) {
        this.users = users;
    }
}