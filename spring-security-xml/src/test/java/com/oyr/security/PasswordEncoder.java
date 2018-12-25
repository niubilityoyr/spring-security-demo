package com.oyr.security;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * Created by Administrator on 2018/12/18.
 */
public class PasswordEncoder {

    @Test
    public void md5() {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        // 21232f297a57a5a743894a0e4a801fc3 admin md5 加密后得到
        // d9e214f539198aa3acd7081206c98d15 admin md5 加盐后得到
        String adminPassword = md5.encodePassword("admin", "admin");
        System.out.println(adminPassword);
    }

}
