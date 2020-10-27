package com.yankaizhang.springframework.test.controller;


import com.yankaizhang.springframework.annotation.Controller;
import com.yankaizhang.springframework.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("username", "haha");
        return "index";
    }

}
