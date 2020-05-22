package com.example.demo.service.impl;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.entity.Todo;
import com.example.demo.dao.mapper.TodoMapper;
import com.example.demo.service.TodoService;

/**
 * @author yeqiang
 * @since 5/21/20 4:02 PM
 */
@Component
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

}
