package test.controller;

import com.yankaizhang.springframework.annotation.Autowired;
import com.yankaizhang.springframework.annotation.Controller;
import com.yankaizhang.springframework.annotation.RequestMapping;
import com.yankaizhang.springframework.annotation.RequestParam;
import test.service.TestService;
import com.yankaizhang.springframework.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/hello")
    public ModelAndView hello(HttpServletRequest req, HttpServletResponse resp, @RequestParam("name") String name){
        String result = testService.sayHello(name);
        HashMap<String, Object> map = new HashMap<>();
        map.put("answer", result);
        ModelAndView modelAndView = new ModelAndView("hello", map);     // 返回到hello.html
        return modelAndView;
    }
}
