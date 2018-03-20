package com.yodean.oa.common.enums;

/**
 * Created by rick on 2018/3/20.
 */
public enum CategoryEnum {
    TASK("任务"),
    MEETING("会议"),
    NOTE("便签");

    private String description;

    CategoryEnum(String description) {
        this.description = description;
    }


}
