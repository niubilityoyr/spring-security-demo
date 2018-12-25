package com.oyr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/12/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BCryptPasswordEncoderTest {

    @Test
    public void encode() { // 加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // $2a$10$/e8VuiJVG3hZad3GFoFYkecMBgQ3C3iArq7uF4h.Y3U.MHfIz9Le6
        // $2a$10$Z7u6./Zso.yZ4f5Bq7oX3O6St/HyE2uDSPkBcmw41rmVHLHWa1ho2
        String a123456 = passwordEncoder.encode("admin");
        System.out.println(a123456);
    }

    @Test
    public void matches(){ // 比对
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean a123456 = passwordEncoder.matches("a123456", "$2a$10$Z7u6./Zso.yZ4f5Bq7oX3O6St/HyE2uDSPkBcmw41rmVHLHWa1ho2");
        System.out.println(a123456);
    }

}
