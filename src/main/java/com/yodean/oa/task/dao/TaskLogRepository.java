package com.yodean.oa.task.dao;

import com.yodean.oa.task.entity.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 4/2/18.
 */
public interface TaskLogRepository extends JpaRepository<TaskLog, Integer> {
}
