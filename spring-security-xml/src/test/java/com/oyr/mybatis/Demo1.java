package com.oyr.mybatis;

import com.oyr.security.dao.UserMapper;
import com.oyr.security.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Create by 欧阳荣
 * 2018/12/17 0:44
 */
public class Demo1 {

    public static void main(String[] args) {
        ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*");
        UserMapper userMapper = application.getBean(UserMapper.class);
        User user = userMapper.findByUsername("admin");
        System.out.println(user);
    }

}
