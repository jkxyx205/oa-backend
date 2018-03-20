package com.yodean.oa.sys.label.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by rick on 2018/3/20.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ColorEnum {
    COLOR_1("#f00"),
    COLOR_2("#ff0"),
    COLOR_3("#f0f"),
    COLOR_4("#0ff");

    private String color;

    ColorEnum(String color) {
        this.color = color;
    }

    public String getName() {
        return name();
    }

    public String getColor() {
        return color;
    }
}
