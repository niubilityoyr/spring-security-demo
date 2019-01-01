package com.oyr;

import java.time.LocalDateTime;

/**
 * Created by Administrator on 2018/12/25.
 */
public class LocalDateTimeTest {

    public static void main(String[] args) throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        boolean before = LocalDateTime.now().isAfter(now);
        System.out.println(before);
    }

}
