package com.example.demo.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.entity.Todo;
import com.example.demo.form.TodoAddForm;
import com.example.demo.service.TodoService;
import com.example.demo.vo.ResponseVO;

/**
 * @author yeqiang
 * @since 5/21/20 3:21 PM
 */
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @RequestMapping("/")
    public void index() {}

    @RequestMapping("add")
    public ResponseVO add(@RequestBody TodoAddForm todoAddForm) {
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoAddForm, todo);
        todoService.save(todo);
        return null;
    }

    @RequestMapping("del")
    public ResponseVO del() {
        return null;
    }

    @RequestMapping("list")
    public ResponseVO list() {
        return null;
    }

    @RequestMapping("view")
    public ResponseVO view() {
        return null;
    }

    @RequestMapping("update")
    public ResponseVO update() {
        return null;
    }
}
