package com.yodean.oa.sys.util;

import com.yodean.oa.sys.user.entity.User;

/**
 * Created by rick on 3/27/18.
 */
public class UserUtils {
    /**
     * 获取当前用户
     * @return 取不到返回 new User()
     */
    public static User getUser(){
        User user = new User();
        user.setId(1);
        user.setChineseName("Rick.Xu");
//        Principal principal = getPrincipal();
//        if (principal!=null){
//            User user = get(principal.getId());
//            if (user != null){
//                return user;
//            }
//            return new User();
//        }
//        // 如果没有登录，则返回实例化空的User对象。
//        return new User();
        return user;
    }
}
