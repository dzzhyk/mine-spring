package com.yankaizhang.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 方法元数据的一个标准实现
 * @author dzzhyk
 */

public class StandMethodMetadata implements MethodMetadata {

    private final Method introspectedMethod;
    private final Annotation[] annotations;

    public StandMethodMetadata(Method introspectedMethod) {
        this.introspectedMethod = introspectedMethod;
        this.annotations = introspectedMethod.getAnnotations();
    }


    @Override
    public String getMethodName() {
        return this.introspectedMethod.getName();
    }

    @Override
    public String getDeclaringClassName() {
        return this.introspectedMethod.getDeclaringClass().getName();
    }

    @Override
    public String getReturnTypeName() {
        return this.introspectedMethod.getReturnType().getName();
    }

    @Override
    public boolean isPrivate() {
        return Modifier.isPrivate(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(this.introspectedMethod.getModifiers());
    }

    @Override
    public boolean isOverridable() {
        return !isFinal() && !isPrivate() && !isStatic();
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.annotations;
    }
}
