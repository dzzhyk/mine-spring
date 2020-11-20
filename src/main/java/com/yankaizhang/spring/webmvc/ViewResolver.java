package com.yankaizhang.spring.webmvc;

import java.io.File;

/**
 * 根据模板名称选择合适的模板解析引擎
 * @author dzzhyk
 */
public class ViewResolver {

    /** 默认的模板文件目录前缀，默认解析WEB-INF目录级下的jsp */
    public static final String DEFAULT_TEMPLATE_PREFIX = "/WEB-INF/";

    /** 默认解析jsp模板 */
    public static final String DEFAULT_TEMPLATE_SUFFIX = ".jsp";

    /** 实际使用的前缀 */
    private final String prefix;

    /** 实际使用的后缀 */
    private final String suffix;

    /** 项目根目录 */
    public static String PROJECT_DIR = null;

    /**
     * 默认的构造函数
     */
    public ViewResolver() {
        this.prefix = DEFAULT_TEMPLATE_PREFIX;
        this.suffix = DEFAULT_TEMPLATE_SUFFIX;
    }

    public ViewResolver(String prefix, String suffix){
        this.prefix = (prefix == null? DEFAULT_TEMPLATE_PREFIX : prefix);
        this.suffix = (suffix == null? DEFAULT_TEMPLATE_SUFFIX : suffix);
    }

    /**
     * 解析视图名称，返回可能的视图对象
     * @param viewName 视图名称
     * @return 视图对象
     */
    public View resolveViewName(String viewName) {
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(this.suffix) ? viewName : (viewName + this.suffix);

        // 找到这个名字的模板文件，如果找不到返回null
        File templateFile = new File((PROJECT_DIR + File.separator + this.prefix + File.separator + viewName)
                .replaceAll("/+", "/"));
        return (templateFile.exists() ? new View(templateFile) : null);
    }

    @Override
    public String toString() {
        return "ViewResolver{" +
                "prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }
}
