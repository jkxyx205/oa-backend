package com.yodean.oa.sys.user.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by rick on 2018/3/15.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping
    public Result<User> save(@RequestBody  User user) {
        return ResultUtil.success(userService.save(user));
    }

    @GetMapping
    public Result<User> findAll() {
        return ResultUtil.success(userService.findAll());
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return user;
    }
/*
    @GetMapping("/name/{name}")
    public User getUserById(@PathVariable String name) {
        return userService.findByChineseName(name);
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
    }*/


}