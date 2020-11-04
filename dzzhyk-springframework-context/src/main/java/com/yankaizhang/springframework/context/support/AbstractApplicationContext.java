package com.yankaizhang.springframework.context.support;

import com.yankaizhang.springframework.context.ApplicationContext;

/**
 * IOC容器实现类的顶层抽象类
 * 只实现了公共方法
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    // 只提供给子类重写
    public void refresh() throws Exception {}
}
