package com.example.demo.dao.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author yeqiang
 * @since 5/21/20 3:10 PM
 */
@Data
public class Todo {
    /**
     * MySQL 采用bigint 自增情况下，必须设置id类型，否则会默认注入一个非常大的数
     */
    @TableId(type = IdType.AUTO)
    Long id;
    String content;
    Date gmt_create;
    Date gmt_modified;
}
