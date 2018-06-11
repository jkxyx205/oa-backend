package com.yodean.oa.sys.user.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtils;
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
    public Result<Integer> save(@RequestBody User user) {
        return ResultUtils.success(userService.save(user));
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody User user, @PathVariable Integer id) {
        userService.update(user, id);
        return ResultUtils.success();
    }

    @GetMapping
    public Result<User> findAll() {
        return ResultUtils.success(userService.findAll());
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return user;
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResultUtils.success();
    }

}