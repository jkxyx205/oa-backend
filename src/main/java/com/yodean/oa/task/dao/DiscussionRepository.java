package com.yodean.oa.task.dao;

import com.yodean.oa.common.dao.ExtendedRepository;
import com.yodean.oa.task.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 4/3/18.
 */
public interface DiscussionRepository extends ExtendedRepository<Discussion, Integer> {

}
