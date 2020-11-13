package com.yankaizhang.springframework.test.controller;


import com.yankaizhang.springframework.beans.factory.annotation.Autowired;
import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.test.service.TestService;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;
import com.yankaizhang.springframework.webmvc.annotation.RequestParam;
import com.yankaizhang.springframework.webmvc.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    public void hi(){
        System.out.println("执行了hi方法...");
        System.out.println(testService.sayHello("dzzhyk"));
    }

    /**
     * 文件上传示例
     */
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        //获取文件的真实文件名
        String trueName = file.getOriginalFilename();
        if (null == trueName){
            request.setAttribute("msg", "上传失败");
            return "index";
        }
        String DIR = "/Users/dzzhyk/Desktop/openSource/springframework/dzzhyk-springframework-test/upload/";
        String saveName = UUID.randomUUID().toString();

        //创建要保存的文件
        File dir = new File(DIR);
        File newFile = new File(DIR + saveName + trueName.substring(trueName.lastIndexOf(".")));

        if (!dir.exists()){
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs){
                request.setAttribute("msg", "上传失败");
                return "index";
            }
        }

        //把临时文件file转储到newFile上
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("msg", "上传失败");
            return "index";
        }
        request.setAttribute("msg", "上传成功");
        return "index";
    }

    /**
     * 文件下载示例
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response,
                           @RequestParam("filename") String filename) throws IOException {
        File file = new File("/Users/dzzhyk/Desktop/openSource/springframework/dzzhyk-springframework-test/upload"
                + File.separator + filename);

        if (!file.exists()) {
            return;
        }
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);

        byte[] buffer = new byte[1024];

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = response.getOutputStream()
        ) {
            // 文件下载
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            os.flush();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
