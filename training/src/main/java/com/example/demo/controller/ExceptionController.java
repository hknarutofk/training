package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.api.R;

/**
 * 异常测试用控制器，用于引发一个异常
 * 
 * @author yeqiang
 * @since 5/26/20 10:28 AM
 */
@RestController
@RequestMapping("exception")
public class ExceptionController {
    @RequestMapping("test")
    public void test() {
        throw new RuntimeException("test");
    }

    @RequestMapping("test_ok")
    public R test_ok() {
        return R.ok(null);
    }
}
