package com.yankaizhang.spring.beans;


/**
 * 元数据接口
 * 实现了这个接口的对象可以获取元数据
 * @author dzzhyk
 * @since 2020-11-28 13:53:02
 */
public interface BeanMetadataElement {

	/**
	 * 获取这个对象的配置源
	 * @return 配置源对象
	 */
	default Object getSource() {
		return null;
	}

}
