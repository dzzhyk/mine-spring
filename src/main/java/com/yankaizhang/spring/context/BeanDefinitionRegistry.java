package com.yankaizhang.spring.context;

import com.yankaizhang.spring.beans.BeanDefinition;

/**
 * 这个接口定义了操作BeanDefinition的基本方法
 * 这个接口被容器实现，可以使容器具有操作BeanDefinitionMap的能力
 *
 * 当然，目前是简化的版本，而且IoC容器没有进行分层，还不太复杂
 * @author dzzhyk
 */

public interface BeanDefinitionRegistry {

    /**
     * 注册一个BeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws Exception;

    /**
     * 移除一个BeanDefinition
     */
    void removeBeanDefinition(String beanName) throws Exception;

    /**
     * 获取BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws Exception;

    /**
     * 根据名称检查是否包含某个BeanDefinition
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 返回BeanDefinition的所有名字
     */
    String[] getBeanDefinitionNames();

    /**
     * 返回BeanDefinition总数
     */
    int getBeanDefinitionCount();

    /**
     * 检查名字是否已经被占用
     */
    boolean isBeanNameInUse(String beanName);


}
