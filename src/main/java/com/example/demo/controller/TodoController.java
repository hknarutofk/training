package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.entity.Todo;
import com.example.demo.service.ITodoService;

/**
 * <p>
 * 代办项目 前端控制器
 * </p>
 *
 * @author yeqiang
 * @since 2020-05-22
 */
@RestController
@RequestMapping("/todo")
public class TodoController extends ApiController {
    @Autowired
    ITodoService todoService;

    @PostMapping("add")
    public R add(@RequestBody Todo todo) {
        todoService.save(todo);
        return success(todo);
    }
}
