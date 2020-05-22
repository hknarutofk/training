package com.example.demo.vo;

import lombok.Data;

/**
 * @author yeqiang
 * @since 5/21/20 3:50 PM
 */
@Data
public class ResponseVO {
    int code;
    String message;
    String log;
    Object data;
}
