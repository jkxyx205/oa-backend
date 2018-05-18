package com.yodean.oa.property.meetingroom.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 5/16/18.
 */
@Entity
@Table(name = "t_meeting_room")
public class MeetingRoom extends DataEntity {

    /**
     * 会议室名称
     */
    private String title;

    /**
     * 会议室容积
     */
    private Integer capacity;

    /**
     * 会议室器材
     */
    private String equipment;

    /**
     * 会议室地址
     */
    private String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

