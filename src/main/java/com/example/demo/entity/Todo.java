package com.example.demo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 代办项目
 * </p>
 *
 * @author yeqiang
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Todo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content;

    private Integer status;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;


}
