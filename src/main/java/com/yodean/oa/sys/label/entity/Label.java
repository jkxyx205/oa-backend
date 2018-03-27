package com.yodean.oa.sys.label.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.common.enums.CategoryEnum;
import com.yodean.oa.sys.label.enums.ColorEnum;
import com.yodean.oa.task.entity.Task;
import org.hibernate.annotations.Filter;

import javax.persistence.*;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "sys_label")
public class Label extends DataEntity {

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


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryEnum category;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

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
}
