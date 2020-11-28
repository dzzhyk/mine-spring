package com.yankaizhang.spring.core.type;


/**
 * MethodMetadata接口定义了方法元数据的一些获取方法
 * @author dzzhyk
 * @since 2020-11-28 13:46:56
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {
    /**
     * 获取方法名
     * @return 方法名
     */
    String getMethodName();

    /**
     * 获取声明该方法的类对象名称
     * @return 类名
     */
    String getDeclaringClassName();

    /**
     * 获取该方法返回类型的类名
     * @return 类名
     */
    String getReturnTypeName();

    /**
     * 判断该方法是否为私有方法
     * @return 判断结果
     */
    boolean isPrivate();

    /**
     * 判断该方法是否为抽象方法
     * @return 判断结果
     */
    boolean isAbstract();

    /**
     * 判断该方法是否为静态方法
     * @return 判断结果
     */
    boolean isStatic();

    /**
     * 判断该方法是否为final方法
     * @return 判断结果
     */
    boolean isFinal();

    /**
     * 判断该方法是否是可重写的
     * @return 判断结果
     */
    boolean isOverridable();
}
