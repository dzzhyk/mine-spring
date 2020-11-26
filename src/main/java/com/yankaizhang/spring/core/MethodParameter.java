package com.yankaizhang.spring.core;


import com.yankaizhang.spring.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 * 简易的方法的参数包装类
 * @author dzzhyk
 */
@SuppressWarnings("all")
public class MethodParameter {

    /** 包装的方法对象 */
    private final Method method;

    /** 该参数的类型 */
    private Class<?> parameterType;

    /** 该参数的名称 */
    private String parameterName;

    /** 该参数的位置 */
    private final int parameterIndex;

    /** 该参数的注解列表 */
    private Annotation[] parameterAnnotations;

    /** 空注解列表常数 */
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    public MethodParameter(Method method, int parameterIndex) {
        assert method != null;
        this.method = method;
        assert parameterIndex >= -1 && parameterIndex <= (method.getParameterCount()-1);
        this.parameterIndex = parameterIndex;
    }

    public MethodParameter(int parameterIndex) {
        this.method = null;
        this.parameterIndex = parameterIndex;
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?> getParameterType() {
        if (this.parameterType == null){
            if (this.parameterIndex < 0) {
                // 返回值
                if (this.method != null) {
                    this.parameterType = this.method.getReturnType();
                }else{
                    this.parameterType = void.class;
                }
            }
            // 普通参数
            this.parameterType = this.method.getParameterTypes()[this.parameterIndex];
        }
        return this.parameterType;
    }

    public String getParameterName() {
        if (this.parameterIndex < 0) {
            return null;
        }
        if (this.parameterName == null){
            Parameter[] parameters = this.method.getParameters();
            this.parameterName = parameters[this.parameterIndex].getName();
        }
        return this.parameterName;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public Annotation[] getParameterAnnotations() {
        if (this.parameterAnnotations == null){
            Annotation[][] annotations = this.method.getParameterAnnotations();
            this.parameterAnnotations =
                    (this.parameterIndex >= 0) && (this.parameterIndex < this.method.getParameterCount()-1)
                            ? annotations[this.parameterIndex]
                            : EMPTY_ANNOTATION_ARRAY;
        }
        return this.parameterAnnotations;
    }

    /**
     * 获取参数的某个注解
     */
    public <T extends Annotation> T getParameterAnnotation(Class<T> annotationClass){
        Annotation[] parameterAnnotations = getParameterAnnotations();
        for (Annotation annotation : parameterAnnotations) {
            if (annotationClass.isInstance(annotation)){
                return (T) annotation;
            }
        }
        return null;
    }

    /**
     * 判断该参数是否有某个注解
     */
    public <T extends Annotation> boolean hasParameterAnnotation(Class<T> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameter that = (MethodParameter) o;
        return parameterIndex == that.parameterIndex &&
                method.equals(that.method) &&
                Objects.equals(parameterType, that.parameterType) &&
                Objects.equals(parameterName, that.parameterName) &&
                Arrays.equals(parameterAnnotations, that.parameterAnnotations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, parameterType, parameterName, parameterIndex);
        result = 31 * result + Arrays.hashCode(parameterAnnotations);
        return result;
    }


    /**
     * 用于表示返回参数的内置类
     */
    public static class ReturnValueMethodParameter extends MethodParameter{

        private Object returnValue;

        public ReturnValueMethodParameter(HandlerMethod method, Object returnValue) {
            super(method.getMethod(), -1);
            this.returnValue = returnValue;
        }

        public Object getReturnValue() {
            return returnValue;
        }

        public void setReturnValue(Object returnValue) {
            this.returnValue = returnValue;
        }

        protected ReturnValueMethodParameter(ReturnValueMethodParameter original) {
            super(original.getMethod(), -1);
            this.returnValue = original.returnValue;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new ReturnValueMethodParameter(this);
        }
    }
}
