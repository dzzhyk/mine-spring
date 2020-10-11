package com.yankaizhang.springframework.aop;

import lombok.Data;

/**
 * AOP配置封装类
 */

@Data
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
}
