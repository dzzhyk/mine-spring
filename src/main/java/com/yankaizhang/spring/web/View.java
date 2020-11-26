package com.yankaizhang.spring.web;

import com.yankaizhang.spring.webmvc.ModelAndView;
import com.yankaizhang.spring.webmvc.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板视图接口，定义了模板视图的操作
 * @author dzzhyk
 */
public interface View {

    /**
     * 渲染视图
     * @param mav {@link ModelAndView}对象
     * @param req 请求
     * @param resp 响应
     * @throws Exception 处理视图过程中的异常
     */
    void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 获取该种视图的contentType值
     * @return contentType字符串
     */
    String getContentType();
}
