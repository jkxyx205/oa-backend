package com.yodean.oa.common.enums;

/**
 * Created by rick on 2018/3/20.
 */
public enum Category {
    TASK("任务"),
    TASK_DISCUSSION("任务讨论"),
    MEETING("会议"),
    NOTE("便签");

    private String description;

    Category(String description) {
        this.description = description;
    }


}
