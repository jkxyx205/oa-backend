package com.yodean.oa.sys.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by rick on 4/12/18.
 */
@RestController
@RequestMapping
public class LogoController {

    @GetMapping("/login")
    public String login(String username, String password,String vcode,Boolean rememberMe){
        System.out.println(username);

        if (Objects.isNull(username) || Objects.isNull(password)) {
            return "loginError";
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        SecurityUtils.getSubject().login(token);

//        SecurityUtils.getSubject().hasRole("admin");





        return "loginSuccess";
    }
}
