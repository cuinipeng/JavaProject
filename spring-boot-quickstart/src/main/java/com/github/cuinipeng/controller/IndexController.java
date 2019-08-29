package com.github.cuinipeng.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "项目主页")
@Controller
public class IndexController {

    @ApiOperation("主页接口")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

}
