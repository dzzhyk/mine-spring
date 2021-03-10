package com.yankaizhang.spring.beans.factory;

/**
 * 工厂bean对象的接口，目前暂时没用上
 * TODO: 仿照Spring实现完整内容
 * @author dzzhyk
 * @since 2020-11-28 13:52:41
 */
public interface FactoryBean<T> {

    /**
     * 获取工厂创建的对象
     * @return 产品
     * @throws Exception 创建过程异常
     */
    T getObject() throws Exception;
}
