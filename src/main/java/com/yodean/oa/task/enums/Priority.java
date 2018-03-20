package com.yodean.oa.task.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by rick on 2018/3/19.
 */
public enum  Priority {

    PRIORITY_NORMAL("0", "普通"),
    PRIORITY_URGENT("1", "紧急"),
    PRIORITY_VERY_URGENT("2", "非常紧急");

    private String id;

    private String title;

    Priority(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @JsonValue
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
