package com.yankaizhang.spring.aop;

/**
 * AOP配置封装类
 * @author dzzhyk
 * @since 2020-11-28 13:55:22
 */
public class AopConfig implements Cloneable {

    private String pointCut;
    /**
     * 切面类名
     */
    private String aspectClass;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectAfterThrow;
    /**
     * 需要通知的异常类型
     */
    private String aspectAfterThrowingName;

    /**
     * 深拷贝
     */
    @Override
    public AopConfig clone() throws CloneNotSupportedException {
        AopConfig clone = (AopConfig) super.clone();
        clone.setPointCut(pointCut);
        clone.setAspectClass(aspectClass);
        clone.setAspectBefore(aspectBefore);
        clone.setAspectAfter(aspectAfter);
        clone.setAspectAfterThrow(aspectAfterThrow);
        clone.setAspectAfterThrowingName(aspectAfterThrowingName);
        return clone;
    }

    public String getPointCut() {
        return pointCut;
    }

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }

    public String getAspectBefore() {
        return aspectBefore;
    }

    public void setAspectBefore(String aspectBefore) {
        this.aspectBefore = aspectBefore;
    }

    public String getAspectAfter() {
        return aspectAfter;
    }

    public void setAspectAfter(String aspectAfter) {
        this.aspectAfter = aspectAfter;
    }

    public String getAspectAfterThrow() {
        return aspectAfterThrow;
    }

    public void setAspectAfterThrow(String aspectAfterThrow) {
        this.aspectAfterThrow = aspectAfterThrow;
    }

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }

    public void setAspectAfterThrowingName(String aspectAfterThrowingName) {
        this.aspectAfterThrowingName = aspectAfterThrowingName;
    }
}
