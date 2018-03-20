package com.yodean.oa.task.dao;

import com.yodean.oa.task.entity.TaskParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 2018/3/20.
 */
public interface TaskParticipantRepository extends JpaRepository<TaskParticipant, Integer> {
}
