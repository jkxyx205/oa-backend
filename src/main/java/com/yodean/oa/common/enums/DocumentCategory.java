package com.yodean.oa.common.enums;

/**
 * Created by rick on 4/9/18.
 */
public enum  DocumentCategory {
    TASK("任务"),
    TASK_DISCUSSION("任务讨论"),
    MEETING("会议"),
    NOTE("便签"),
    DISK_ME("资料库-我的"),
    DISK_SHARE("资料库-共享"),
    DISK_COMPANY("资料库-公司")
    ;

    private String description;

    DocumentCategory(String description) {
        this.description = description;
    }
}
