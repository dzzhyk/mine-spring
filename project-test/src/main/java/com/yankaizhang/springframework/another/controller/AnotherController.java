package com.yankaizhang.springframework.another.controller;

import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;

/**
 * 测试@ComponentScan包扫描
 * @author dzzhyk
 */
@Controller
public class AnotherController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
