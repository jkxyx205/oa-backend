package com.yodean.oa.sys.label.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.label.enums.ColorEnum;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "sys_label")
@DynamicUpdate
public class Label extends DataEntity {

    @JsonIgnore
    @Embedded
    private LabelId labelId;

    /***
     * 标签颜色
     */
    @Enumerated(EnumType.STRING)
    private ColorEnum color = ColorEnum.COLOR_1;

    /***
     * 便签名称
     */
    @Column(nullable = false)
    private String title;


    public Label() {
    }

    public ColorEnum getColor() {
        return color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LabelId getLabelId() {
        return labelId;
    }

    public void setLabelId(LabelId labelId) {
        this.labelId = labelId;
    }

    public static enum LabelCategory {
        TASK("任务"),
        MEETING("会议"),
        NOTE("便签"),
        NOTICE("公告");

        private String description;

        LabelCategory(String description) {
            this.description = description;
        }


    }

    @Embeddable
    public static class LabelId {
        @Column(name = "category")
        @Enumerated(EnumType.STRING)
        private LabelCategory category;

        @Column(name = "category_id")
        private Integer categoryId;

        public LabelId(){}

        public LabelId(LabelCategory category, Integer categoryId) {
            this.categoryId = categoryId;
            this.category = category;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public LabelCategory getCategory() {
            return category;
        }

        public void setCategory(LabelCategory category) {
            this.category = category;
        }
    }
}
