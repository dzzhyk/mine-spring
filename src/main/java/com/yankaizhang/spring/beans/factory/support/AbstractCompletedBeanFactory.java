package com.yankaizhang.spring.beans.factory.support;

import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;

/**
 * 具有全部功能的bean工厂抽象类
 * 继承自{@link AbstractConfigurableBeanFactory}抽象类，额外实现了全功能接口{@link CompletedBeanFactory}
 * 添加了默认的自动装配功能实现、默认的遍历功能实现
 * @author dzzhyk
 * @since 2020-12-21 18:33:16
 */
public class AbstractCompletedBeanFactory extends AbstractConfigurableBeanFactory
        implements CompletedBeanFactory {
}
