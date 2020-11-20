package project.test.controller;


import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;
import com.yankaizhang.springframework.webmvc.annotation.RequestParam;
import com.yankaizhang.springframework.webmvc.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author dzzhyk
 */
@Controller
public class TestController {

    private final String TEMP_DIR = "/Users/dzzhyk/Desktop/openSource/springframework/project-test/upload/";
    // 源文件名称 - 上传文件名称
    private Map<String, String> FILE_MAP = new HashMap<>();

    @RequestMapping({"/", "/index"})
    public String index(HttpServletRequest request){
        request.setAttribute("msg", "你好！");
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * 文件上传
     */
    @RequestMapping("/upload")
    public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        //获取文件的真实文件名
        String trueName = file.getOriginalFilename();
        if (null == trueName){
            request.setAttribute("msg", "上传失败");
            return "index.jsp";
        }

        String saveName = UUID.randomUUID().toString();

        //创建要保存的文件
        File dir = new File(TEMP_DIR);
        File newFile = new File(TEMP_DIR + saveName + trueName.substring(trueName.lastIndexOf(".")));

        if (!dir.exists()){
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs){
                request.setAttribute("msg", "上传失败");
                return "index.jsp";
            }
        }

        //把临时文件file转储到newFile上
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("msg", "上传失败");
            return "index.jsp";
        }
        request.setAttribute("msg", "上传成功");
        FILE_MAP.put(trueName, saveName);
        return "index.jsp";
    }

    /**
     * 文件下载
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response,
                         @RequestParam("filename") String filename) throws IOException {
        File file = new File(TEMP_DIR + File.separator + filename);

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

    @RequestMapping("/listFile")
    public String listFile(HttpServletRequest request){
        request.setAttribute("fileMap", FILE_MAP);
        return "index.jsp";
    }

}
