package com.yodean.oa.property.seal.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rick on 6/1/18.
 */
@Entity
@Table(name = "t_seal")
public class Seal extends DataEntity {

    public static final Character AVAILABLE = '1';

    public static final Character FORBIDDEN = '0';

    /**
     * 印章名称
     */
    private String title;

    /**
     * 保管人或保管部门
     */
    private Integer keeper;

    /**
     * 允许使用人
     */
    private String users;

    /**
     * 印章状态
     */
    private Character status;

    /**
     * 是否关联审批
     */
    private Boolean needApprove;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Boolean getNeedApprove() {
        return needApprove;
    }

    public void setNeedApprove(Boolean needApprove) {
        this.needApprove = needApprove;
    }
}
