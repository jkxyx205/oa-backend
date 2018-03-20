package com.yodean.oa.task.service;

import com.yodean.oa.task.dao.TaskParticipantRepository;
import com.yodean.oa.task.entity.TaskParticipant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/20.
 */
@Service
public class TaskParticipantService {

    @Resource
    TaskParticipantRepository taskParticipantRepository;

    public TaskParticipant save(TaskParticipant taskParticipant) {
        return taskParticipantRepository.save(taskParticipant);
    }
}
