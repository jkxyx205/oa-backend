package com.yodean.oa.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yodean.oa.common.config.Global;
import com.yodean.oa.common.enums.ResultCode;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.sys.util.UserUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by rick on 2018/3/15.
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataEntity<T> implements Serializable {

    public static String DEL_FLAG_NORMAL = "1";

    public static String DEL_FLAG_REMOVE = "0";

    public static String DEL_FLAG_CLEAN = "2"; //彻底删除


    public static  <T> T of(Class<T> tClass, Integer id) {
        T t;
        try {
            t = tClass.newInstance();
            PropertyUtils.setProperty(t, Global.ENTITY_ID,  id);
        } catch (Exception e) {
            throw new OAException(ResultCode.UNKNOW_ERROR, e);
        }
        return t;

    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @JsonIgnore
    @Column(name = "create_by", updatable = false)
    private String createBy;

    @JsonIgnore
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date", nullable = false, updatable = false)
    private Date createDate;

//    @JsonIgnore
    @Column(name = "update_by", nullable = false)
    private String updateBy;

//    @JsonIgnore
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="update_date", nullable = false)
    private Date updateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remarks;

    @JsonIgnore
    @Column(name="del_flag", length = 1, nullable = false)
    private String delFlag;

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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @PrePersist
    @PreUpdate
    private void changeUpdateInfo() {
        Date now = new Date();

        if (this.getId() == null) {
            this.setCreateBy(UserUtils.getUser().getId() + "");
            this.setCreateDate(now);
            this.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
        }

        this.setUpdateBy(UserUtils.getUser().getId() + "");
        this.setUpdateDate(now);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (obj instanceof DataEntity) {
            DataEntity dataEntity = (DataEntity)obj;
            if (new EqualsBuilder().append(dataEntity.id, id).isEquals())
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
