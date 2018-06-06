package com.yodean.oa.property.material.entity;

import com.yodean.oa.common.entity.DataEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by rick on 5/29/18.
 * 入库凭证
 */
@Entity
@Table(name = "t_material_incoming")
@DynamicUpdate
public class Incoming extends DataEntity {

    /**
     * 批号
     */
    private String batchNum;

    /**
     * 物料id
     */
    private Integer materialId;

    /**
     * 入库数量
     */
    private Double num;

    /**
     * 入库单位
     */
    private Integer unitId;


    /**
     *  仓库数量
     */
    private Double baseNum;

    /**
     *  仓库基本单位
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_unit_id")
    private ConversionUnit baseUnit;

    /**
     * 入库库位Id
     */
    private Integer storageId;

    /**
     * 保管人或保管部门
     */
    private Integer keeper;

    /**
     * 允许使用人
     */
    private String users;

    /**
     * 是否关联审批
     */
    private boolean needApprove;

    /**
     * 设备需要序列号
     */
    private String sno;

    /**
     * 状态
     * 0 禁用
     * 1 启用
     * 2 报废
     * 3 在库
     * 4 借出
     */
    private Character status;

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Double getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(Double baseNum) {
        this.baseNum = baseNum;
    }

    public ConversionUnit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(ConversionUnit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getKeeper() {
        return keeper;
    }

    public void setKeeper(Integer keeper) {
        this.keeper = keeper;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public boolean isNeedApprove() {
        return needApprove;
    }

    public void setNeedApprove(boolean needApprove) {
        this.needApprove = needApprove;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
