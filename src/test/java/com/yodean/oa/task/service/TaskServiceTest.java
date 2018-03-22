package com.yodean.oa.task.service;

import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.task.entity.Task;
import com.yodean.oa.task.enums.Priority;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by rick on 2018/3/19.
 */


@RunWith(SpringRunner.class)
@SpringBootTest

public class TaskServiceTest {
    @Resource
    private TaskService taskService;

    @Test
    public void testSave() throws Exception {
        Task task = new Task();
        task.setId(8);
        task.setTitle("我的测试任务2222");
        task.setContent("去玩儿吧2222222222");
        task.setPriority(Priority.PRIORITY_VERY_URGENT);
        Date now = new Date();
        task.setUpdateDate(now);
        task.setCreateDate(now);
        task.setTipStartDate(now);
        task.setTipEndDate(now);
        task.setStartDate(now);
        task.setEndDate(now);
        task.setRemarks("this is a remark!!");


        User user = new User();
        user.setId(1);
        user.setName("BBBBB");

        Set<User> participants = new HashSet<>(1);
        participants.add(user);

//        task.setParticipants(participants);

        participants.forEach((user1) -> user.getId());

        taskService.save(task);

    }

    @Test
    public void testFindById() {
        Integer id = 8;
        Task task = taskService.findById(id);
        Assert.assertEquals("我的测试任务1", task.getTitle());
        System.out.println(task);

    }


}