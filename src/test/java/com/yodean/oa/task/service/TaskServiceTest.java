package com.yodean.oa.task.service;

import com.yodean.oa.task.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by rick on 5/9/18.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void findById() throws Exception {
        Task task = taskService.findById(1);
    }

    @Test
    public void deleteDiscussion() {
        taskService.deleteDiscussion(1);
    }

}