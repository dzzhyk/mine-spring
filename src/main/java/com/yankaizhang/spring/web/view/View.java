package com.yankaizhang.spring.web.view;

import com.yankaizhang.spring.web.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模板视图接口，定义了模板视图的操作
 * @author dzzhyk
 * @since 2020-11-28 13:43:51
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
