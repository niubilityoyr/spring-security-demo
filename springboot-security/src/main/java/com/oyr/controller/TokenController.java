package com.oyr.controller;

import com.oyr.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 * Created by Administrator on 2018/12/17.
 */
@RequestMapping("/token")
@Controller
public class TokenController {


    // TODO 没有导入相关jar包会出错！！！
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

}
