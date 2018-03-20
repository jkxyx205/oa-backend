package com.yodean.oa.sys.label.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "sys_label_relationship")
public class LabelRelationShip extends DataEntity {

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "label_id")
    private Integer labelId;

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }
}
