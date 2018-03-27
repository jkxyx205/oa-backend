package com.yodean.oa.meeting.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Entity;

/**
 * Created by rick on 3/27/18.
 */
@Entity(name = "t_meeting_user")
public class MeetingUser extends DataEntity {

    private Integer meetingId;

    private Integer userId;

    /***
     * 是否必须参加
     */
    private Boolean isTo;

    public MeetingUser() {
    }

    public MeetingUser(Integer meetingId, Integer userId, Boolean isTo) {
        this.meetingId = meetingId;
        this.userId = userId;
        this.isTo = isTo;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getTo() {
        return isTo;
    }

    public void setTo(Boolean to) {
        isTo = to;
    }
}
