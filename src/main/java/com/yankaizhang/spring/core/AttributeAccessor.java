package com.yankaizhang.spring.core;

/**
 * AttributeAccessor接口定义了元数据MetaData的访问方法
 * @author dzzhyk
 * @since 2020-11-28 13:47:12
 */
public interface AttributeAccessor {

	/**
	 * 设置属性
	 * @param name 属性名称
	 * @param value 属性值
	 */
	void setAttribute(String name,  Object value);

	/**
	 * 获取属性
	 * @param name 属性名称
	 * @return 属性值
	 */
	Object getAttribute(String name);

	/**
	 * 移除属性
	 * @param name 属性名称
	 * @return 移除的属性值
	 */
	Object removeAttribute(String name);

	/**
	 * 判断是否具有指定属性
	 * @param name 属性名
	 * @return 判断结果
	 */
	boolean hasAttribute(String name);

	/**
	 * 返回属性的所有Name
	 * @return 当前所有属性名称列表
	 */
	String[] attributeNames();

}
