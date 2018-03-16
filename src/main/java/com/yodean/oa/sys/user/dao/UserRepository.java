package com.yodean.oa.sys.user.dao;

import com.yodean.oa.sys.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by rick on 2018/3/15.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    //TODO 这也行都不用实现...
    public User findByName(String name);
}
