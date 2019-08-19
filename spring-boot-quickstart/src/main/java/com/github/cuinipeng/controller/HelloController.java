package com.github.cuinipeng.controller;

import java.text.MessageFormat;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private String template = "<a>https://spring.io/projects/spring-boot</a> by {0}";

    @Cacheable("default")
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "vendor", defaultValue = "Spring Boot") String vendor) {
        return MessageFormat.format(template, vendor);
    }

}
