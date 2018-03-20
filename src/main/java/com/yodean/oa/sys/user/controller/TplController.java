package com.yodean.oa.sys.user.controller;

import com.yodean.oa.sys.user.entity.User;
import com.yodean.oa.sys.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rick on 2018/3/16.
 */
@Controller
@RequestMapping("/tpl")
@ApiIgnore
public class TplController {

    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public String gotoIndex(Model model) {
        List<User> list = userService.findAll();
        model.addAttribute("list", list);
        return "/index";
    }
}
