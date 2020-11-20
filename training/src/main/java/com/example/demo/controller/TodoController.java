package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;

import io.swagger.annotations.Api;

/**
 * <p>
 * 代办项目 前端控制器
 * </p>
 *
 * @author yeqiang
 * @since 2020-05-22
 */
@Api("代办API")
@RestController
public class TodoController extends ApiController {
    @Autowired
    TodoService todoService;

    @GetMapping("todo")
    public HttpEntity<Todo> view(@RequestParam("id") Long id) {
        Todo todo = todoService.getById(id);
        // todo.add(linkTo(methodOn(TodoController.class).view(id)).withSelfRel());
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping
    public R add(@RequestBody Todo todo) {
        todoService.save(todo);
        return success(todo);
    }

    @PutMapping
    public R update(@RequestBody Todo todo) {
        todoService.updateById(todo);
        return success(todo);
    }

    @GetMapping
    public R list() {
        return null;
    }

    @DeleteMapping("del")
    public R del() {
        return null;
    }

}
