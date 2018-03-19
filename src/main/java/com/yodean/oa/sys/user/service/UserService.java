package com.yodean.oa.sys.user.service;

import com.yodean.oa.common.enums.ResultEnum;
import com.yodean.oa.common.exception.OAException;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.user.dao.UserMapper;
import com.yodean.oa.sys.user.dao.UserRepository;
import com.yodean.oa.sys.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by rick on 2018/3/15.
 */
@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    @Resource
    private UserMapper userMapper;

    @Resource
    private BaseService baseService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        //getOne findOne findByID??
        //TODO
        Optional<User> optional =userRepository.findById(id);

        if(!optional.isPresent()) {
            throw new OAException(ResultEnum.NOT_FOUND_ERROR);
        }



        return optional.get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    //test transaction
    @Transactional
    public void insertTwo() {
        User user1 = new User();
        user1.setName("C");
        save(user1);
        int a = 2 / 0;

        updateStatus();

//        User user2 = new User();
//        user2.setName("Japan");
//        save(user2);
    }

    public void updateStatus() {
        String sql = "update sys_user set name = ? where id = 1";
        jdbcTemplate.update(sql, new Object[] {"lisi"});


    }

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public List<User> getAllUsers2() {

        return userMapper.getAllUsers2();
    }

//    public List<User> getAllUsers3() {
//        String sql = "select name from sys_user where id = :id";
//        Map<String, Object> param = new HashMap<String, Object>(1);
////        param.put("id", 1);
//
//
//        Map<String, Object> formatMap = new HashMap<String, Object>();
//        String formatSql = SqlFormatter.formatSql(sql, param, formatMap);
//
//        Object[] args = NamedParameterUtils.buildValueArray(formatSql,
//                formatMap);
//        formatSql = formatSql.replaceAll(SqlFormatter.PARAM_REGEX,"?"); //mysql
//
//
//        List<User> userList = baseService.jdbcTemplate.query(formatSql, args, new BeanPropertyRowMapper<User>(User.class));
//        return userList;
//    }


}
