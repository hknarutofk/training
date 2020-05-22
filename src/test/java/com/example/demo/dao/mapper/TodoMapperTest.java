package com.example.demo.dao.mapper;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.entity.Todo;

/**
 * @author yeqiang
 * @since 5/21/20 3:16 PM
 */
@SpringBootTest
class TodoMapperTest {

    @Autowired
    TodoMapper todoMapper;

    @Test
    public void testMapper() {
        Todo todo = new Todo();
        todo.setContent("todo1");
        todo.setGmt_create(new Date());
        todo.setGmt_modified(new Date());
        System.out.println(todoMapper.insert(todo));
        System.out.println(todo.getId());
    }
}