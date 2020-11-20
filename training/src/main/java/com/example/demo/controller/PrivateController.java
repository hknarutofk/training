package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeqiang
 * @since 11/18/20 3:44 PM
 */
@RestController
@RequestMapping("private")
public class PrivateController {

    @RequestMapping("sth")
    @PreAuthorize("hasRole('ADMIN')")
    // @Secured("ROLE_ADMIN")
    public String sth() {
        return SecurityContextHolder.getContext().getAuthentication().toString();
    }
}
