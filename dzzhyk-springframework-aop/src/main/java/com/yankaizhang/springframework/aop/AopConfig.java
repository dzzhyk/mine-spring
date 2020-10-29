package com.yankaizhang.springframework.aop;

/**
 * AOP配置封装类
 */

public class AopConfig implements Cloneable {

    private String pointCut;
    private String aspectClass; // 切面类名
    private String aspectBefore;
    private String aspectAfter;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName; // 需要通知的异常类型

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
