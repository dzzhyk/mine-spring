package project.test.controller;

import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dzzhyk
 */
@Controller
@RequestMapping("/another")
public class AnotherController {

    @RequestMapping({"", "/", "/index"})
    public String index(HttpServletRequest request){
        request.setAttribute("msg", "another!");
        return "index";
    }

}
