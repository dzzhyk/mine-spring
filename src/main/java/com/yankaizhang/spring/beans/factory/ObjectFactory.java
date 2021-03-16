package com.yankaizhang.spring.beans.factory;

/**
 * 函数式接口<br/>
 * 该接口定义了一个对象工厂，常常作为API方法被使用，而实现了{@link FactoryBean}接口的对象一般是作为实例对象使用<br/>
 * @author dzzhyk
 * @since 2020-12-22 23:19:44
 */
@FunctionalInterface
public interface ObjectFactory<T> {


	/**
	 * 生产一个对象实例
	 * @return 实例对象
	 * @throws RuntimeException 工厂生产过程异常
	 */
	T getObject() throws RuntimeException;

}
