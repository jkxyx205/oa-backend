package com.yodean.oa.task.enums;

import com.yodean.oa.sys.util.UserUtils;

/**
 * Created by rick on 4/2/18.
 */
public enum TaskLogType {
    TASK(UserUtils.getUser().getChineseName() + " 创建了任务"),
    USER_ADD(UserUtils.getUser().getChineseName() + " 添加了参与者 %s"),
    USER_REMOVE(UserUtils.getUser().getChineseName() + " 移除了参与者 %s"),
    TITLE(UserUtils.getUser().getChineseName() + " 更新了标题"),
    CONTENT(UserUtils.getUser().getChineseName() + " 更新了正文"),
    PRIORITY(UserUtils.getUser().getChineseName() + " 更新任务优先级为 %s"),
    REPEATE(UserUtils.getUser().getChineseName() + " 更新了提醒");

    private String logName;

    private TaskLogType(String logName) {
        this.logName = logName;
    }

    public String getLogName() {
        return logName;
    }
}
