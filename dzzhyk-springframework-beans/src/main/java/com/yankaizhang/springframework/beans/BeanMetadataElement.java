package com.yankaizhang.springframework.beans;


/**
 * 元数据接口
 * 实现了这个接口的对象可以获取元数据
 */
public interface BeanMetadataElement {

	default Object getSource() {
		return null;
	}

}
