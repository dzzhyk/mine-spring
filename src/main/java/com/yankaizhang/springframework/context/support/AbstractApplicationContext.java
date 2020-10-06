package com.yankaizhang.springframework.context.support;

/**
 * IOC容器实现类的顶层抽象类
 * 只实现了公共方法
 */
public abstract class AbstractApplicationContext {
    // 只提供给子类重写
    public void refresh() throws Exception {}
}
