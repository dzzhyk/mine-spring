package com.yankaizhang.springframework.core.type;


/**
 * MethodMetadata接口定义了方法元数据的一些获取方法
 */
public interface MethodMetadata extends AnnotatedTypeMetadata {
    String getMethodName();
    String getDeclaringClassName();
    String getReturnTypeName();
    boolean isPrivate();
    boolean isAbstract();
    boolean isStatic();
    boolean isFinal();
    boolean isOverridable();
}
