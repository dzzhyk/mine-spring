package com.yankaizhang.springframework.core.type;


/**
 * ClassMetadata接口定义了Class的元数据
 */
public interface ClassMetadata {

    String getClassName();
    boolean isInterface();
    boolean isAnnotation();
    boolean isAbstract();
    /**
     * 是否为具体实现类
     */
    default boolean isConcrete(){
        return !(isAbstract() || isInterface());
    }
    boolean isFinal();

    /**
     * 判断类是否独立
     * 这里独立的意思就是：是否为可直接使用闭包创建的顶级类或者静态内部类
     */
    boolean isIndependent();

    /**
     * 获取创建该闭包类的类
     * 如果不是闭包创建的，就是自己的类
     */
    String getEnclosingClassName();
    default boolean hasEnclosingClass(){
        return getEnclosingClassName() != null;
    }

    /**
     * 获取父类
     */
    String getSuperClass();
    default boolean hasSuperClass(){
        return getSuperClass() != null;
    }

    String[] getInterfaceNames();

    /**
     * 获取成员类名
     */
    String[] getMemberClassNames();
}
