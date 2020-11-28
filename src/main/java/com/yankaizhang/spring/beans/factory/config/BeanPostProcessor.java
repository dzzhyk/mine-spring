package com.yankaizhang.spring.beans.factory.config;

/**
 * bean初始化的回调处理器
 * 继承增强这个处理器，可以实现更多功能
 * 这个类还可以作为抽象类，有很多增强实现类，在初始化之前先判断bean是否有这样的类，如果有，在前后阶段执行里面的方法
 * @author dzzhyk
 * @since 2020-11-28 13:52:03
 */
public class BeanPostProcessor {

    /**
     * 前置处理bean
     * @param bean  待处理bean对象
     * @param beanName bean名称
     * @return 前置处理后的bean对象
     * @throws Exception 处理中异常
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }

    /**
     * 后置处理bean
     * @param bean  待处理bean对象
     * @param beanName bean名称
     * @return 后置处理后的bean对象
     * @throws Exception 处理中异常
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception{
        return bean;
    }
}
