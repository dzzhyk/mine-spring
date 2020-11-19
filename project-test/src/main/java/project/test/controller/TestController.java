package project.test.controller;


import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dzzhyk
 */
@Controller
public class TestController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        request.setAttribute("msg", "haha");
        return "index";
    }

}
