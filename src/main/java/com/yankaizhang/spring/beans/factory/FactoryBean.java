package com.yankaizhang.spring.beans.factory;

/**
 * 工厂bean对象的接口<br/>
 * 实现这个接口的bean对象会被当做工厂对象使用，而不能作为单纯的bean对象而使用了
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


    /**
     * 工厂创建对象是否为单例
     * @return 默认为单例
     */
    default boolean isSingleton(){
        return true;
    }
}
