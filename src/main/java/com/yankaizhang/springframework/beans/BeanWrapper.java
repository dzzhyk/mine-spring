package com.yankaizhang.springframework.beans;

/**
 * bean包装类
 * 用来封装创建后的bean对象实例，代理对象或者原生对象都由这个BeanWrapper来保存
 */
public class BeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * @return 代理以后的Class ($Proxy0)
     */
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
