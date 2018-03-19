package com.yodean.oa.sys.user.controller;

import com.yodean.oa.common.dto.Result;
import com.yodean.oa.common.util.ResultUtil;
import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import io.swagger.annotations.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by rick on 2018/3/15.
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户模块")
public class UserController {

    @Resource
    private UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(paramType="path",name="id",dataType="int",required=true,value="用户的id",defaultValue="1"),
            @ApiImplicitParam(paramType="query",name="page",dataType="int",required=true,value="当前页面",defaultValue="1")
            })
    @ApiResponses({
               @ApiResponse(code=400,message="请求参数没填好"),
               @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id, Integer page) {
//        User user = new User();
//        user.setId(id);
//        user.setName("Rick.Zhang => " + configuration.getCdn() + ",page => " + page);
//        return user;

//        System.out.println("hot.......111");
        User user = userService.findById(id);
//        user.setName("Sheet");
        return user;
    }

    @GetMapping("/name/{name}")
    public User getUserById(@PathVariable String name) {
        return userService.findByName(name);
    }


    @ApiOperation(value="获取用户列表", notes="获取用户列表备注")
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

    @ApiIgnore
    @RequestMapping("/insertTwo")
    public void insertTwo() {
        userService.insertTwo();
    }
}
