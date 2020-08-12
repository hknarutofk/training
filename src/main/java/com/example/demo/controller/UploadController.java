package com.example.demo.controller;

import java.io.File;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yeqiang
 * @since 8/5/20 4:20 PM
 */
@RestController
@Slf4j
public class UploadController {
    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file) throws Exception {
        file.transferTo(new File("/tmp/b.bin"));
    }
}
