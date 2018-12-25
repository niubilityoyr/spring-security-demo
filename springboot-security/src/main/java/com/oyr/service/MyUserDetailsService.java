package com.oyr.service;

import com.oyr.domain.Permission;
import com.oyr.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("当前账号不存在");
        }

        // 如果用户不为空，查询权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Permission> permissionList = permissionService.findListByUserId(user.getId());
        for (Permission permission : permissionList) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getTag());
            authorities.add(grantedAuthority);
        }
        user.setAuthorities(authorities);

        return user;
    }

}
