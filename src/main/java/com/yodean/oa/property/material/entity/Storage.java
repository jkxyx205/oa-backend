package com.yodean.oa.property.material.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by rick on 5/24/18.
 */
@Entity
@Table(name = "t_material_storage")
public class Storage extends DataEntity {

    public static final Character STATUS_AVAILABLE = '1';

    public static final Character STATUS_FORBIDDEN = '0';

    /**
     * 库位Id
     */
    private String sid;

    /**
     * 库位名称
     */
    private String title;

    /**
     * 状态
     * '0' 禁用
     * '1' 启用
     */
    private Character status = '1';

    /**
     * 父库存
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Storage parentStorage;

    /**
     * 接收参数
     */
    @Transient
    private Integer parentId;

    /**
     * 子库存
     */
    @OneToMany(mappedBy = "parentStorage")
    private List<Storage> storageList = new ArrayList<>();

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Storage getParentStorage() {
        return parentStorage;
    }


    public void setParentStorage(Storage parentStorage) {
        this.parentStorage = parentStorage;
    }

    @PrePersist
    @PreUpdate
    public void injectParams() {
        if (Objects.nonNull(parentId)) {
            parentStorage = (parentStorage == null ? new Storage() : parentStorage);
            parentStorage.setId(parentId);
        }

    }

    public List<Storage> getStorageList() {
        return storageList;
    }

    public void setStorageList(List<Storage> storageList) {
        this.storageList = storageList;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
