package com.yankaizhang.spring.webmvc.servlet;

import com.yankaizhang.spring.context.AnnotationConfigApplicationContext;
import com.yankaizhang.spring.web.ViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dzzhyk
 * DispatcherServlet的抽象父类
 * @since 2020-11-28 13:40:25
 */
public abstract class FrameworkServlet extends HttpServlet {

    /** 配置文件的默认位置 */
    private final String LOCATION = "contextConfigLocation";

    /** 用来存储配置文件对象 */
    private Properties configProperties = new Properties();

    /** 配置文件中的扫描包路径 */
    private static final String BASE_PACKAGE = "context.basePackage";

    /** 内置IoC容器 */
    private AnnotationConfigApplicationContext context;

    public FrameworkServlet() {}

    @Override
    public void init(ServletConfig config) {
        String location = config.getInitParameter(LOCATION);
        // 目前现在这里读取出basePackage
        InputStream ins = this.getClass()
                .getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
        try {
            configProperties.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != ins){
                try { ins.close(); } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 初始化IoC容器
        context = new AnnotationConfigApplicationContext(configProperties.getProperty(BASE_PACKAGE));
        // 初始化模板解析器中的项目根目录
        ViewResolver.PROJECT_DIR = config.getServletContext().getRealPath("/");
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

}
