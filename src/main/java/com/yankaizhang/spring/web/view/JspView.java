package com.yankaizhang.spring.web.view;

import com.yankaizhang.spring.web.model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个简化后的jspView实现，继承了{@link View}
 * @author dzzhyk
 * @since 2020-11-28 13:43:46
 */
public class JspView extends AbstractView {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public JspView() {}

    public JspView(String url) {
        super(url);
    }

    @Override
    public void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 如果是jsp文件就直接转发吧
        String url = getUrl();
        log.debug("[Jsp文件渲染] 渲染至 : " + url);
        req.getRequestDispatcher(url).forward(req, resp);

    }
}
