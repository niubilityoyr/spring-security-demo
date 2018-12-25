package com.oyr.config;

import com.oyr.filter.BeforeLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2018/12/21.
 */
@EnableWebSecurity  // 开启spring security web支持
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 主要做拦截请求

        // 认证的一些设置
        http.formLogin().loginPage("/authentication/require")
                .loginProcessingUrl("/authentication/login").usernameParameter("username").passwordParameter("password")
                .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler);

        // 记住我的一些设置
        http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService)
                .rememberMeParameter("remember_me").rememberMeCookieName("security-remember")
                .tokenValiditySeconds(180);

        // 登出的一些设置
        http.logout().logoutUrl("/logout").permitAll().logoutSuccessHandler(logoutSuccessHandler);

        // 403处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // 权限的一些设置
        http.authorizeRequests()
                .antMatchers("/authentication/require", "/login", "/loginError", "/logoutSuccess", "/403", "/validateCode/code").permitAll() // 权限的一些设置url
                .antMatchers("/order/**").hasRole("USER") // 指定要访问/order/** 都必须要有user
                .antMatchers("/admin/**").hasRole("ADMIN") // 指定要访问/admin/** 都必须要有admin
                .anyRequest().authenticated() // 指定后面的所有请求都需要身份认证
                .and()
                .csrf().disable(); // 关闭csrf

        // 在 UsernamePasswordAuthenticationFilter 前添加 BeforeLoginFilter
        http.addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 配置user-detail服务
        // 从db中取数据信息使用 userDetailsService，并且指定一个密码加密器
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
       /*
       // 设置一个密码加密器，不然程序会报错，定义两个用户存在内存中，密码必须要加密一下
       auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER")
                .and().
                withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");*/
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
