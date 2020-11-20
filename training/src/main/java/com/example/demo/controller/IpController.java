package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeqiang
 * @since 8/17/20 3:35 PM
 */
@RestController
public class IpController {
    @RequestMapping("ip")
    public String ip(HttpServletRequest request) {

        return request.getRemoteHost() + "-" + request.getRemoteAddr() + "-" + request.getHeader("X-Forwarded-For");
    }
}
