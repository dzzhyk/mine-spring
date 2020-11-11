package com.yankaizhang.springframework.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * 根据模板名称选择合适的模板解析引擎
 * @author dzzhyk
 */
public class ViewResolver {

    /**
     * 默认解析html模板
     */
    public static final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;
    private String viewName;

    public ViewResolver(String templateRoot, String viewName){
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
        this.viewName = viewName;
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception{
        if (null == viewName || "".equals(viewName.trim())) return null;

        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        // 找到这个名字的模板文件xxx.html
        File templateFile = new File((templateRootDir.getPath() + File.separator + viewName).replaceAll("/+", "/"));
        return new View(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
