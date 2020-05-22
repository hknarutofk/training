package com.example.demo.form;

import java.util.Date;

import lombok.Data;

/**
 * @author yeqiang
 * @since 5/21/20 3:55 PM
 */
@Data
public class TodoAddForm {

    String content;
    Date gmt_create;
    Date gmt_modified;

}
