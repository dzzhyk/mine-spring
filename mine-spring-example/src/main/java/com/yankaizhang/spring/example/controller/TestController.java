package com.yankaizhang.spring.example.controller;

import com.yankaizhang.spring.context.annotation.Controller;
import com.yankaizhang.spring.webmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dzzhyk
 */
@Controller
public class TestController {

    @RequestMapping({"/", "/index"})
    public String index(){
        return "index";
    }

}
