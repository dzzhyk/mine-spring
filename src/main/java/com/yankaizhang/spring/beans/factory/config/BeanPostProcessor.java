package com.yankaizhang.spring.beans.factory.config;

/**
 * bean初始化处理器接口
 * 实现这个处理器，从而可以对bean对象实例化的过程进行处理
 * Initialization 表示初始化,对象已经生成
 * @author dzzhyk
 * @since 2020-12-02 14:55:10
 */
public interface BeanPostProcessor {

    /**
     * 在Bean的自定义初始化方法之前执行
     * @param bean  待处理bean对象
     * @param beanName bean名称
     * @return 前置处理后的bean对象
     * @throws Exception 处理中异常
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }


    /**
     * 在Bean的自定义初始化方法执行完成之后执行
     * @param bean  待处理bean对象
     * @param beanName bean名称
     * @return 后置处理后的bean对象
     * @throws Exception 处理中异常
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }
}
