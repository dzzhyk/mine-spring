package com.yankaizhang.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 注解元数据的标准实现
 *
 * 继承了StandardClassMetadata，说明可以操作类
 * 实现了AnnotationMetadata，说明可以操作注解
 */

public class StandardAnnotationMetadata extends StandardClassMetadata implements AnnotationMetadata {

    private final Annotation[] annotations;

    public StandardAnnotationMetadata(Class<?> introspectedClass) {
        super(introspectedClass);
        this.annotations = introspectedClass.getAnnotations();
    }

    /**
     * 获取使用了某个注解的所有方法
     */
    @Override
    public Set<MethodMetadata> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
        HashSet<MethodMetadata> result = new HashSet<>(4);
        Class<?> introspectedClass = getIntrospectedClass();
        Method[] declaredMethods = introspectedClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(annotationClass)) {
                result.add(new StandMethodMetadata(declaredMethod));
            }
        }
        return result;
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.annotations;
    }
}
