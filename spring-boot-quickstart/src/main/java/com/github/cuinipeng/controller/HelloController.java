package com.github.cuinipeng.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(path = "/hello")
    public String hello() {
        return "<h3>Hello, Spring Boot!</h3>";
    }

}
