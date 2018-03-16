package com.yodean.oa.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rick on 2018/3/16.
 */
public class TestInterceptor implements HandlerInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(TestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("interceptor 【preHandle】 url => " + request.getRequestURL());

        //sessionID是在第一次使用（request.getSession()）的时候创建，并添加到Cookies中返回
        logger.info("username from session => " + request.getSession().getAttribute("username"));
        logger.info("sessionID => " + request.getSession().getId());

        Cookie[] cookies = request.getCookies();
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                logger.info("cookies => " + cookie.getName() + "," + cookie.getValue());

                Cookie ck = new Cookie("given","123");
                response.addCookie(ck);
            }
        }


//        throw new OAException(ResultEnum.TOKEN_ERROR);//如果有错误直接抛出异常

        request.getSession().setAttribute("username", "rick");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logger.info("interceptor 【postHandle】 url => " + request.getRequestURL());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        logger.info("interceptor 【afterCompletion】 url => " + request.getRequestURL());
    }
}
