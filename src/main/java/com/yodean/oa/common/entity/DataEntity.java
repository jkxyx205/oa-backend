package com.yodean.oa.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by rick on 2018/3/15.
 */
@MappedSuperclass
public class DataEntity implements Serializable {
    public static Character DEL_FLAG_NORMAL = '1';

    public static Character DEL_FLAG_REMOVE = '0';

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "create_by", updatable = false)
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createDate;

    @Column(name = "update_by", nullable = false)
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="update_date", nullable = false)
    private Date updateDate;

    private String remarks;

    @Column(name="del_flag", length = 1, nullable = false)
    private Character delFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Character getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Character delFlag) {
        this.delFlag = delFlag;
    }

    @PrePersist
    @PreUpdate
    private void changeUpdateInfo() {
        Date now = new Date();
        String curUser = "admin";
        if (this.getId() == null) {
            this.setCreateBy(curUser);
            this.setCreateDate(now);
            this.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
        }

        this.setUpdateBy(curUser);
        this.setUpdateDate(now);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
