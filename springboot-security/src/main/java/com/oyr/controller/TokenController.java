package com.oyr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/17.
 */
@RequestMapping("/token")
@Controller
public class TokenController {


    /*// TODO 没有导入相关jar包会出错！！！
    @RequestMapping("")
    @ResponseBody
    public User findUserByToken(){
        // 获取当前登录的用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // 获取当前登录的用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 权限
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return user;
    }
*/
}
