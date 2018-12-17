package com.oyr.security.service;

import com.oyr.security.dao.PermissionMapper;
import com.oyr.security.dao.UserMapper;
import com.oyr.security.domain.Permission;
import com.oyr.security.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by 欧阳荣
 * 2018/12/16 21:02
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    //登陆验证时，通过username获取用户的所有权限信息， 
    //并返回User放到spring的全局缓存SecurityContextHolder中，以供授权器使用 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("用户开始认证-------");

        // 用户所拥有的权限
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // 根据username从数据库中获取信息
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("当前用户信息不存在！！！");
        }

        // 如果用户不为空，查询权限
        List<Permission> permissionList = permissionMapper.findByUserId(user.getId());
        for (Permission permission : permissionList) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getTag());
            authorities.add(grantedAuthority);
        }
        user.setAuthorities(authorities);

        log.info("user permission：{}", user.getAuthorities());
        return user;
    }

}
