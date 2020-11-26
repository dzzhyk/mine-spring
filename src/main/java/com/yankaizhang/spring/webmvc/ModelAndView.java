package com.yankaizhang.spring.webmvc;

import com.yankaizhang.spring.util.CollectionUtils;
import com.yankaizhang.spring.web.View;

import java.util.Map;

/**
 * 封装要返回的模板和参数
 * @author dzzhyk
 */
public class ModelAndView {

    /** 视图对象 */
    private Object view;

    /** 视图属性map */
    private Map<String, ?> model;

    /** 当前mav是否没有任何内容 */
    private boolean cleared;

    public ModelAndView() {}

    public ModelAndView(String viewName){
        this.view = viewName;
    }

    public ModelAndView(Object view) {
        this.view = view;
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.view = viewName;
        this.model = model;
    }

    public ModelAndView(Object view, Map<String, ?> model) {
        this.view = view;
        this.model = model;
    }

    public String getViewName(){
        return (this.view instanceof String? (String) this.view : null);
    }

    public void setViewName(String viewName){
        this.view = viewName;
    }

    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    public void setView(Object view) {
        this.view = view;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
    
    public boolean hasView(){
        return this.view != null;
    }

    public boolean isEmpty(){
        return this.view==null && CollectionUtils.isEmpty(model);
    }

    public boolean isCleared() {
        return cleared && isEmpty();
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    @Override
    public String toString() {
        return "ModelAndView{" +
                "view=" + view +
                '}';
    }
}
