package com.yankaizhang.spring.beans.factory;

/**
 * 函数式接口
 * 该接口定义了一个对象工厂
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
