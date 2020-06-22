package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

class DemoApplicationTests {

    @Test
    void contextLoads() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        Date d = new Date(1263370065244368898l);
        System.out.println(d);
        System.out.println(simpleDateFormat.format(d));
        System.out.println(System.currentTimeMillis());
    }

}
