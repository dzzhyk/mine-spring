package com.yankaizhang.spring.context;

import com.yankaizhang.spring.beans.factory.HierarchicalBeanFactory;
import com.yankaizhang.spring.beans.factory.ListableBeanFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 应用上下文的核心接口<br/>
 * 实现了{@link ListableBeanFactory}与{@link HierarchicalBeanFactory}两个接口说明该上下文是可以遍历容器实例，允许继承的<br/>
 * @author dzzhyk
 * @since 2020-11-28 13:51:33
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory {
}
