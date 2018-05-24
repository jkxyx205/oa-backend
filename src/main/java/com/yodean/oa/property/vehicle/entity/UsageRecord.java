package com.yodean.oa.property.vehicle.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;

/**
 * Created by rick on 5/21/18.
 * 车辆使用纪录
 */
@Entity
@Table(name = "t_vehicle_record")
public class UsageRecord extends DataEntity {

    /**
     * 经办人
     */
    private Integer userId;

    /**
     * 目的地
     */
    private String dist;

    /**
     * 返成日期
     */
    private String backDate;

    /**
     * 出车类型
     */
    @Column(name = "usage_type")
    @Enumerated(EnumType.STRING)
    private UseType useType;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle = new Vehicle();

    @Transient
    private Integer vehicleId;

    public enum UseType {
        TRAFFIC("出行"), MAINTENANCE("维修"), KEEP("保养");

        private String description;

        UseType(String description) {
            this.description = description;
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public UseType getUseType() {
        return useType;
    }

    public void setUseType(UseType useType) {
        this.useType = useType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
}
