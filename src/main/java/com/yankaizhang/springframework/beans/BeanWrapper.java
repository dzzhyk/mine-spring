package com.yankaizhang.springframework.beans;

/**
 * bean包装类
 * 用来封装创建后的bean对象实例，代理对象或者原生对象都由这个BeanWrapper来保存
 */
public class BeanWrapper {

    private Object wrappedInstance; // 包装对象
    private Class<?> wrappedClass;  // 包装对象目标最终的实现类

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass(); // 只有在创建最初包装对象的时候，保存目标类
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    /**
     * @return 代理以后的Class ($Proxy0)
     */
    public Class<?> getWrappedClass() {
        return wrappedClass;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

}
