package com.yankaizhang.spring.webmvc.context;

import com.yankaizhang.spring.beans.factory.Aware;

import javax.servlet.ServletContext;

/**
 * 实现了这个接口，你就可以获取ServletContext这个对象
 * @author dzzhyk
 */
public interface ServletContextAware extends Aware {

    /**
     * 设置web容器上下文
     * @param context web容器上下文对象
     */
    void setServletContext(ServletContext context);
}
