package com.yankaizhang.springframework.webmvc;

import java.util.Map;

/**
 * 封装要返回的模板和参数
 * @author dzzhyk
 */
public class ModelAndView {
    private String viewName;
    private Map<String, ?> model;

    public ModelAndView(String viewName) {
        this(viewName, null);
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
