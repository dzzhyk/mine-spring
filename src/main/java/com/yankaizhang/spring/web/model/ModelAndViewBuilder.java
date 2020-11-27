package com.yankaizhang.spring.web.model;

import com.yankaizhang.spring.web.view.View;

import java.util.Map;

/**
 * ModelAndView建造者
 * @author dzzhyk
 */
public class ModelAndViewBuilder {

    private View view;
    private String viewName;
    private Map<String, ?> model;
    private boolean cleared = true;

    public ModelAndView build(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(view);
        modelAndView.setViewName(viewName);
        modelAndView.setModel(model);
        modelAndView.setCleared(cleared);
        return modelAndView;
    }

    public void from(ModelAndView other){
        this.view = other.getView();
        this.model = other.getModel();
        this.viewName = other.getViewName();
        this.cleared = other.isCleared();
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<?, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
}
