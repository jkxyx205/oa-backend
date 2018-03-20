package com.yodean.oa.task.dao;

import com.yodean.oa.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 2018/3/19.
 */
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
