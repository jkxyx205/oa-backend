package com.yodean.oa.sys.workspace.enums;

/**
 * Created by rick on 3/27/18.
 */
public enum CategoryStatus {
    INBOX("待办"),
    ARCHIVE("归档"),
    TRASH("回收站"),
    DELETE("彻底删除");
    private String description;

    private CategoryStatus(String description) {
        this.description = description;
    }
}
