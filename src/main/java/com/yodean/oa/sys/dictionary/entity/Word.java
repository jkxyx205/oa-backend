package com.yodean.oa.sys.dictionary.entity;

import com.yodean.oa.common.entity.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by rick on 5/17/18.
 */
@Entity
@Table(name = "sys_dictionary",
        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "name"})})
public class Word extends DataEntity {

    private String category;

    private String name;

    private String description;

    private String seq;

    private String remark;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}