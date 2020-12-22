package com.yankaizhang.spring.beans.factory;

/**
 * 可继承的 BeanFactory，是BeanFactory的进一步实现接口
 * 实现了该接口的工厂对象可以获取父类工厂对象
 * @author dzzhyk
 * @since 2020-12-21 18:25:02
 */
public interface HierarchicalBeanFactory extends BeanFactory {

    /**
     * 获取父类bean工厂对象
     * @return BeanFactory对象
     */
    BeanFactory getParentBeanFactory();

}
