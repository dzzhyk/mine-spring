package com.yankaizhang.springframework.context.support;

import com.yankaizhang.springframework.context.ApplicationContext;

/**
 * IOC容器实现类的顶层抽象类
 * 只实现了公共方法
 * @author dzzhyk
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    /**
     * 刷新容器内容
     */
    public void refresh(){}
}
