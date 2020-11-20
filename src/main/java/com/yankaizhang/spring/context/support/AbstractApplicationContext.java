package com.yankaizhang.spring.context.support;

import com.yankaizhang.spring.context.ApplicationContext;

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
