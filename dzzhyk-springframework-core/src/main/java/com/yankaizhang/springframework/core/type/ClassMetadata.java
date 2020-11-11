package com.yankaizhang.springframework.core.type;


/**
 * ClassMetadata接口定义了Class的元数据
 * @author dzzhyk
 */
public interface ClassMetadata {

    /**
     * 获取类名
     * @return 类名
     */
    String getClassName();

    /**
     * 判断类对象是否为接口
     * @return 判断结果
     */
    boolean isInterface();

    /**
     * 判断类对象是否为注解类
     * @return 判断结果
     */
    boolean isAnnotation();

    /**
     * 判断类对象是否是抽象类
     * @return 判断结果
     */
    boolean isAbstract();
    /**
     * 判断类对象是否为具体实现类，而不是抽象类或者接口
     * @return 判断结果
     */
    default boolean isConcrete(){
        return !(isAbstract() || isInterface());
    }

    /**
     * 判断类对象是否为final类型
     * @return 判断结果
     */
    boolean isFinal();

    /**
     * 判断类是否独立
     * 这里独立的意思就是：是否为可直接使用闭包创建的顶级类或者静态内部类
     * @return 判断结果
     */
    boolean isIndependent();

    /**
     * 获取定义该内部类的类名
     * @return 定义当前类对象的类名称
     */
    String getEnclosingClassName();

    /**
     * 判断类是否是内部类
     * @return 判断结果
     */
    default boolean hasEnclosingClass(){
        return getEnclosingClassName() != null;
    }

    /**
     * 获取父类
     * @return 父类类名
     */
    String getSuperClass();

    /**
     * 判断当前类对象是否有父类
     * @return 判断结果
     */
    default boolean hasSuperClass(){
        return getSuperClass() != null;
    }

    /**
     * 获取该类实现的接口
     * @return 接口列表
     */
    String[] getInterfaceNames();

    /**
     * 获取成员类名
     * @return 成员类名列表
     */
    String[] getMemberClassNames();
}
