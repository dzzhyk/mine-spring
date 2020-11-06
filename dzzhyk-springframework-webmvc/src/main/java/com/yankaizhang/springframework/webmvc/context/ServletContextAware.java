package com.yankaizhang.springframework.webmvc.context;

import com.yankaizhang.springframework.beans.factory.annotation.Aware;

import javax.servlet.ServletContext;

/**
 * 实现了这个接口，你就可以获取ServletContext这个对象
 */
public interface ServletContextAware extends Aware {
    void setServletContext(ServletContext context);
}
