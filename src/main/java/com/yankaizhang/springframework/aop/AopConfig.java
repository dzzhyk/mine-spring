package com.yankaizhang.springframework.aop;

import lombok.Data;

/**
 * AOP配置封装类
 */

@Data
public class AopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName; // 需要通知的异常类型

}
