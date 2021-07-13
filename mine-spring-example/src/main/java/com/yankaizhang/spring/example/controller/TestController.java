package com.yankaizhang.spring.example.controller;

import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.context.annotation.Controller;
import com.yankaizhang.spring.example.entity.R;
import com.yankaizhang.spring.example.entity.User;
import com.yankaizhang.spring.example.service.impl.TestServiceImpl;
import com.yankaizhang.spring.webmvc.annotation.RequestBody;
import com.yankaizhang.spring.webmvc.annotation.RequestMapping;
import com.yankaizhang.spring.webmvc.annotation.RequestParam;
import com.yankaizhang.spring.webmvc.annotation.ResponseBody;
import com.yankaizhang.spring.webmvc.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author dzzhyk
 */
@Controller
@Slf4j
public class TestController {

    /** 自动注入属性 */
    @Autowired
    private TestServiceImpl testService;

    private static final String DIR = "E:\\mine-spring\\mine-spring-example\\upload\\";

    @RequestMapping({"/", "/index"})
    public String index(HttpServletRequest request){
        request.setAttribute("msg", "haha!");
        return "index";
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam("name") String name){
        return testService.sayHello(name);
    }


    @RequestMapping("/json1")
    public @ResponseBody User json1(){
        return new User("dzzhyk", "123");
    }


    @RequestMapping("/json2")
    @ResponseBody
    public User json2(){
        return new User("dzzhyk", "123");
    }


    /**
     * 文件上传并且返回json响应体
     */
    @RequestMapping("/upload")
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("username") String name){
        if (file == null) {
            return R.error();
        }
        String fileName = file.getName();
        log.info("文件名 : " + fileName);
        log.info("参数username : " + name);

        //获取文件的真实文件名
        String trueName = file.getOriginalFilename();
        if (null == trueName){
            return R.error();
        }
        String saveName = UUID.randomUUID().toString();

        File dir = new File(DIR);
        File newFile = new File(DIR + saveName + trueName.substring(trueName.lastIndexOf(".")));

        if (!dir.exists()){
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs){
                return R.error();
            }
        }
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error();
        }
        return R.ok();
    }


    /**
     * 获取request和response对象的示例
     */
    @RequestMapping("/param")
    public String param(@RequestParam("p") String name, HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("msg", name);
        response.addCookie(new Cookie("myCookie", name));
        return "index";
    }


    /**
     * 测试请求体
     */
    @RequestMapping("/body")
    public void body(@RequestBody User user){
        System.out.println("获取了包装后的请求体 : " + user);
    }


    /**
     * 推荐使用包装类来接收基本类型数据
     */
    @RequestMapping("/req")
    @ResponseBody
    public R body(@RequestBody List<User> users,
                       @RequestParam("p") Integer page){
        System.out.println("获取了包装后的请求体 : " + users);
        System.out.println("同时获取了额外的请求参数 : " + page);
        System.out.println("请求参数Class : " + page.getClass());
        return R.ok();
    }


    @RequestMapping("/char")
    @ResponseBody
    public R body(@RequestParam("ch") Character ch){

        // "123" -> '1'
        System.out.println("获取了char请求参数 : " + ch);
        System.out.println("char请求参数Class : " + ch.getClass());
        return R.ok();

    }


    /**
     * 检查对于未标记@RequestParam的参数，会尝试创建一个新的默认对象传入
     * 如果是接口类型，则为null
     */
    @RequestMapping("/commons")
    public void commons(User user, List<Integer> commonList){
        System.out.println(user);
        if (commonList == null){
            System.out.println("commonList是接口类型，mine-spring无法为你创建具体对象");
        }
    }
}
