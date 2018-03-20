package com.yodean.oa.sys.label.entity;

import com.yodean.oa.common.entity.DataEntity;
import com.yodean.oa.sys.label.enums.ColorEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by rick on 2018/3/20.
 */
@Entity(name = "sys_label")
public class Label extends DataEntity {

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private ColorEnum color;

    private String title;

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
}
