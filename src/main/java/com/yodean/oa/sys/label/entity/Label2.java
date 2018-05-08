package com.yodean.oa.sys.label.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.label.enums.ColorEnum;
import org.hibernate.annotations.FilterDef;

import javax.persistence.*;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "sys_label2")
//@FilterDef(name = "filterByHotelCode", parameters = {
//        @ParamDef(name = "filterCode", type = "string")
//})
//@Table(
//        uniqueConstraints = {@UniqueConstraint(columnNames={"category", "category_id"})})
public class Label2 extends DataEntity {

    @Embedded
    private LabelId labelId;

    /***
     * 标签颜色
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ColorEnum color;

    /***
     * 便签名称
     */
    @Column(nullable = false)
    private String title;


    public Label2() {
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
        NOTICE("公告"),
        NEWS("新闻");

        private String description;

        LabelCategory(String description) {
            this.description = description;
        }


    }

    @Embeddable
    public static class LabelId {
        @Column(name = "category_id")
        private Integer categoryId;

        @Column(name = "category")
        @Enumerated(EnumType.STRING)
        private LabelCategory category;

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
