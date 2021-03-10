package com.yankaizhang.spring.beans.factory.config;

/**
 * bean对象销毁方法的处理器
 * @author dzzhyk
 * @since 2021-03-08 11:33:03
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在bean对象销毁之前执行的方法
     * @param bean 待销毁bean对象
     * @param beanName bean对象名称
     * @throws Exception 执行过程异常
     */
    void postProcessBeforeDestruction(Object bean, String beanName) throws Exception;
}
