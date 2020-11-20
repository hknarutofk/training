package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试工具，引发异常，导致jvm崩溃
 * 
 * @author yeqiang
 * @since 7/10/20 11:27 AM
 */
@RestController
@Slf4j
public class DieController {
    static List<byte[]> list = new ArrayList<>();

    @GetMapping("die")
    public void die() {
        while (true) {
            log.debug("allocate memory");
            byte[] buff = new byte[1024 * 1024];
            log.debug("append");
            list.add(buff);
            log.debug("{} MB", list.size());
        }
    }

    @GetMapping("die2")
    public void die2() {
        System.exit(2);
    }
}
