package com.yankaizhang.springframework.webmvc.servlet;

import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author dzzhyk
 * DispatcherServlet的抽象父类
 */
public abstract class FrameworkServlet extends HttpServlet {

    // 用来存储配置文件对象
    private Properties configProperties = new Properties();
    private static final String BASE_PACKAGE = "basePackage";
    private final String LOCATION = "contextConfigLocation";

    /**
     * 默认解析html模板
     */
    public static String DEFAULT_TEMPLATE_SUFFIX = ".jsp";

    /**
     * 配置文件中的模板文件后缀
     */
    private final String TEMPLATE_SUFFIX = "suffix";

    /**
     * 项目根目录
     */
    private static String PROJECT_DIR = null;

    /**
     * 内置IoC容器
     */
    private AnnotationConfigApplicationContext context;

    public FrameworkServlet() {
    }

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
        DEFAULT_TEMPLATE_SUFFIX = configProperties.getProperty(TEMPLATE_SUFFIX);
        PROJECT_DIR = config.getServletContext().getRealPath("/");
    }

    public Properties getConfigProperties() {
        return configProperties;
    }

    public static String getBasePackage() {
        return BASE_PACKAGE;
    }

    public static String getDefaultTemplateSuffix() {
        return DEFAULT_TEMPLATE_SUFFIX;
    }

    public static String getProjectDir() {
        return PROJECT_DIR;
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
