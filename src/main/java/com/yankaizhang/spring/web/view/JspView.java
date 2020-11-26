package com.yankaizhang.spring.web.view;

import com.yankaizhang.spring.web.View;
import com.yankaizhang.spring.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个简化后的jspView实现，继承了{@link View}
 * @author dzzhyk
 */
public class JspView extends AbstractView {

    public JspView(String url) {
        super(url);
    }

    @Override
    public void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 如果是jsp文件就直接转发吧
        String url = getUrl();
        req.getRequestDispatcher(url).forward(req, resp);

    }
}
