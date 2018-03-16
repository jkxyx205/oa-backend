package com.yodean.oa.sys.user.service;


import com.yodean.oa.common.dto.Grid;
import com.yodean.oa.common.dto.PageModel;
import com.yodean.oa.common.service.BaseService;
import com.yodean.oa.sys.user.entity.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 2018/3/15.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private BaseService baseService;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void findById() throws Exception {
        User user = userService.findById(1);

        Assert.assertEquals(user.getName(), "rick");
    }

    @Test
    public void getAllUsers() {
        List<User> userList = userService.getAllUsers();
        Assert.assertEquals(3, userList.size());
    }

    @Test
    public void getAllUsers2() {
        List<User> userList = userService.getAllUsers2();
        Assert.assertEquals(3, userList.size());
    }

    @Test
    public void getAllUsers3() {
//        List<User> userList = userService.getAllUsers3();
//        Assert.assertEquals(3, userList.size());
    }

    @Test
    public void getMappingSQL() {
//        MappedStatement mappedStatement = sqlSessionFactory.getConfiguration().getMappedStatement("com.yodean.oa.sys.user.dao.UserMapper.getAllUsers");
//        String sql = mappedStatement.getBoundSql(Collections.emptyMap()).getSql();
//
//        System.out.println(sql);
//        userService.getAllUsers3();
    }

    @Test
    public void testMapObject() {
        String sql = "select id, name from sys_user where id = :id";
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("id", 3);
        List<Map> map = baseService.query(sql, param);
        System.out.println(map);
    }

    @Test
    public void testString() {
        String sql = "select name from sys_user where id = :id";
        Map<String, Object> param = new HashMap<String, Object>(1);
//        param.put("id", 2);
        List<String> map = baseService.query(sql, param, String.class);
        System.out.println(map);
    }

    @Test
    public void testObject() {
        String sql = "select id, name from sys_user where id = :id";
        Map<String, Object> param = new HashMap<String, Object>(1);
//        param.put("id", 2);
        List<User> list = baseService.query(sql, param, User.class);
        System.out.println(list);
    }

    @Test
    public void testObjectFromMapperFile() {
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("id", 2);
        List<User> list = baseService.query(
                baseService.getSQL("com.yodean.oa.sys.user.dao.UserMapper.getAllUsers"),
                param, User.class);

        for(User user : list)
            System.out.println(user.getRemarks());
    }

    @Test
    public void testGrid() {
        String sql = "select id, name from sys_user where id = :id";
        Map<String, Object> param = new HashMap<String, Object>(1);
        param.put("id", 2);

        PageModel pm = new PageModel();
        pm.setPage(1);
        pm.setRows(-1);
        Grid<User> mapGrid = baseService.query(sql, pm, param, User.class);

        System.out.println(mapGrid);

    }
}