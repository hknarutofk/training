package com.example.demo.service.impl;

import com.example.demo.entity.Todo;
import com.example.demo.mapper.TodoMapper;
import com.example.demo.service.TodoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 代办项目 服务实现类
 * </p>
 *
 * @author yeqiang
 * @since 2020-05-28
 */
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

}
