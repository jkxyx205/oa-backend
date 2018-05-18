package com.yodean.oa.property.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.dictionary.core.DictionaryConstraint;
import com.yodean.oa.sys.dictionary.core.WordJpaConverter;
import com.yodean.oa.sys.dictionary.entity.Word;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by rick on 5/16/18.
 */
@Entity
@Table(name = "t_vehicle")
@DynamicUpdate
public class Vehicle extends DataEntity {

    /**
     * 车牌号
     */
    @NotBlank
    private String licence;

    /**
     * 车辆类型
     */
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    /**
     * 品牌型号（从字典中获取）
     */
    @DictionaryConstraint(name = "VEHICLE_BRAND")
    @Convert(converter = WordJpaConverter.class)
    private Word vehicleBrand;

    /**
     * 车辆识别代号
     */
    private String identifyCode;

    /**
     * 发动机号码
     */
    private String engineCode;

    /**
     * 购买日期
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //覆盖全局的 yyyy-MM-dd HH:mm:ss
    private Date buyDate;

    /**
     * 载客数
     */
    private Integer capacity;

    /**
     * 保管人
     */
    private Integer keeper;

    /**
     * 车辆状体
     */
    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;

    public static enum VehicleType {
        SMALL
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum VehicleStatus {

        AVAILABLE("可用"), FORBIDON("禁用"), OUTSITE("出车"), DEPRECATED("废弃"), MAINTENANCE("维修");

        private String description;

        VehicleStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public String getName() {
            return this.name();
        }
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


    public Word getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(Word vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getKeeper() {
        return keeper;
    }

    public void setKeeper(Integer keeper) {
        this.keeper = keeper;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
