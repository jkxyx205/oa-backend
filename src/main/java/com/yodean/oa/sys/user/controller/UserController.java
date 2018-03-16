package com.yodean.oa.sys.user.controller;

import com.yodean.oa.common.Configuration;
import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by rick on 2018/3/15.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private Configuration configuration;

    @Resource
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id, Integer page) {
//        User user = new User();
//        user.setId(id);
//        user.setName("Rick.Zhang => " + configuration.getCdn() + ",page => " + page);
//        return user;

        System.out.println("hot.......111");
        User user = userService.findById(id);
        user.setName("Sheet");
        return user;
    }

    @GetMapping("/name/{name}")
    public User getUserById(@PathVariable String name) {
        return userService.findByName(name);
    }



    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.findAll();
    }

    @PostMapping("/save")
    public Result saveUser(@Valid User user, BindingResult bindingResult) {
        Result result;
        if (bindingResult.hasErrors()) {
            result = ResultUtil.error(0, bindingResult.getFieldError().getDefaultMessage());
        } else {
            result = ResultUtil.success(userService.save(user));
        }

        return result;

    }

    @PutMapping("/update")
    public void  updateUser(User user) {
        userService.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        userService.deleteById(id);
    }

    @RequestMapping("/insertTwo")
    public void insertTwo() {
        userService.insertTwo();
    }
}
