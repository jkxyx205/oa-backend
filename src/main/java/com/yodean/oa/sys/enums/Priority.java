package com.yodean.oa.sys.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by rick on 2018/3/19.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  Priority {
    PRIORITY_NORMAL("普通"),
    PRIORITY_URGENT("紧急"),
    PRIORITY_VERY_URGENT("非常紧急");


    private String title;

    Priority(String title) {
        this.title = title;
    }

    public String getName() {
        return name();
    }

    public String getTitle() {
        return this.title;
    }

}
