package com.oyr.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Create by 欧阳荣
 * 2018/12/16 21:03
 */
public class User implements UserDetails {

    private Integer id;

    private String username;

    private String realname;

    private String password;

    private Date createDate;// 创建日期

    private Date lastLoginTime;// 最后登录日期

    private boolean enabled;// 是否可用

    private boolean accountNonExpired;// 是否过期

    private boolean accountNonLocked;// 是否锁定

    private boolean credentialsNonExpired;// 用户证书是否有效

    // 用户所有权限
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    public User() {
    }

    public User(String username, String realname, String password, Date createDate, Date lastLoginTime, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, List<GrantedAuthority> authorities) {
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.createDate = createDate;
        this.lastLoginTime = lastLoginTime;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = authorities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
