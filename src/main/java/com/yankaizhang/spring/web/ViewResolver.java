package com.yankaizhang.spring.web;

import com.yankaizhang.spring.util.BeanUtils;
import com.yankaizhang.spring.util.StringUtils;
import com.yankaizhang.spring.web.view.AbstractView;
import com.yankaizhang.spring.web.view.JspView;
import com.yankaizhang.spring.web.view.View;
import com.yankaizhang.spring.web.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 根据模板名称选择合适的模板解析引擎
 * @author dzzhyk
 * @since 2020-11-28 13:43:12
 */
public class ViewResolver {

    /** 默认的视图类 */
    private static final Class<?> DEFAULT_VIEWCLASS = JspView.class;

    /** 默认的模板文件目录前缀，默认解析WEB-INF目录级下的jsp */
    private static final String DEFAULT_TEMPLATE_PREFIX = "/WEB-INF/";

    /** 默认解析jsp模板 */
    private static final String DEFAULT_TEMPLATE_SUFFIX = ".jsp";

    /** 实际的视图类 */
    private Class<?> viewClass;

    /** 实际使用的前缀 */
    private String prefix;

    /** 实际使用的后缀 */
    private String suffix;

    /** contentType */
    private String contentType;

    /** 项目根目录 */
    public static String PROJECT_DIR = null;


    /** 内置的默认view对象 */
    private static final View UNRESOLVED_VIEW = new View() {

        @Override
        public void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp){}

        @Override
        public String getContentType() {
            return null;
        }

    };


    public ViewResolver() {
        this.prefix = DEFAULT_TEMPLATE_PREFIX;
        this.suffix = DEFAULT_TEMPLATE_SUFFIX;
        viewClass = DEFAULT_VIEWCLASS;
    }

    /**
     * 自定义创建一个解析器
     * @param prefix url前缀
     * @param suffix url后缀
     */
    public ViewResolver(String prefix, String suffix){
        this.prefix = (prefix == null? DEFAULT_TEMPLATE_PREFIX : prefix);
        this.suffix = (suffix == null? DEFAULT_TEMPLATE_SUFFIX : suffix);
        this.viewClass = DEFAULT_VIEWCLASS;
    }

    /**
     * 解析视图名称，返回可能的视图对象
     * @param viewName 视图名称
     * @return 视图对象
     */
    public View resolveViewName(String viewName) throws Exception {

        if (StringUtils.isEmpty(viewName)) {
            return UNRESOLVED_VIEW;
        }

        View view = buildView(viewName);
        if (null == view){
            view = UNRESOLVED_VIEW;
        }
        return view;
    }

    @Override
    public String toString() {
        return "ViewResolver{" +
                "viewClass=" + viewClass +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Class<?> getViewClass() {
        return viewClass;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 从viewName创建得到一个view对象
     * @param viewName 视图名称
     * @return 构建好的视图对象
     */
    public View buildView(String viewName) throws Exception {
        Class<?> viewClass = getViewClass();
        // 利用反射创建一个viewClass类对象
        AbstractView view = (AbstractView) BeanUtils.instantiateClass(viewClass);

        // 为创建好的视图对象设置属性
        view.setUrl((getPrefix()+ File.separator + viewName + getSuffix()).replaceAll("/+", "/"));

        String contentType = getContentType();
        if (contentType != null) {
            view.setContentType(contentType);
        }
        return view;
    }

    public void setViewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
