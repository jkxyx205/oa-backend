package com.yodean.oa.sys.user.dao;

import com.yodean.oa.sys.user.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by rick on 2018/3/15.
 */
public interface UserMapper {
    List<User> getAllUsers();

    @Select("SELECT id, name FROM sys_user")
    List<User> getAllUsers2();
}
