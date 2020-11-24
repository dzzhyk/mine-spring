package com.yankaizhang.spring.beans;

/**
 * bean定义的某个元数据属性的包装类
 * @author dzzhyk
 */
public class BeanMetadataAttribute implements BeanMetadataElement {

	private final String name;

	private final Object value;

	private Object source;


	public BeanMetadataAttribute(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	/**
	 * 设置设个数据的产生源头
	 */
	public void setSource( Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

}
