package com.yodean.oa.common.enums;

/**
 * Created by rick on 2018/3/20.
 */
public enum Category {
    TASK("任务"),
    MEETING("会议"),
    NOTE("便签"),
    NOTICE("通知");

    private String description;

    Category(String description) {
        this.description = description;
    }


}