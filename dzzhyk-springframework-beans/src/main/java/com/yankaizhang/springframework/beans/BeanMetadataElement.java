package com.yankaizhang.springframework.beans;


/**
 * 元数据接口
 * 实现了这个接口的对象可以获取元数据
 * @author dzzhyk
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
