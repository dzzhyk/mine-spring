package com.yankaizhang.springframework.beans.config;

/**
 * bean初始化的回调处理器
 * 继承增强这个处理器，可以实现更多功能
 * 这个类还可以作为抽象类，有很多增强实现类，在初始化之前先判断bean是否有这样的类，如果有，在前后阶段执行里面的方法
 */
public class BeanPostProcessor {
    /**
     * 前置处理bean
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

    /**
     * 后置处理bean
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }
}
