package com.yankaizhang.springframework.test.controller;


import com.yankaizhang.springframework.beans.factory.annotation.Autowired;
import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.test.service.TestService;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("username", "haha");
        request.setAttribute("msg", testService.sayHello("haha"));
        return "index";
    }

    public void hi(){
        System.out.println("执行了hi方法...");
    }

}
