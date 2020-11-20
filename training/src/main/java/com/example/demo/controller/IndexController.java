package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeqiang
 * @since 11/19/20 9:55 AM
 */
@RestController
public class IndexController {
    @RequestMapping("")
    public String index() {
        return "index..";
    }
}
