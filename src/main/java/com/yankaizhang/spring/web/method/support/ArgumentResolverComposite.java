package com.yankaizhang.spring.web.method.support;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.CollectionUtils;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ArgumentResolver}对象的一个集合类
 * @author dzzhyk
 */
public class ArgumentResolverComposite implements ArgumentResolver {

    /** 列表 */
    private final List<ArgumentResolver> argumentResolvers = new LinkedList<>();

    /** 已经处理过的cache */
    private final Map<MethodParameter, ArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);

    public ArgumentResolverComposite addResolver(ArgumentResolver resolver){
        this.argumentResolvers.add(resolver);
        return this;
    }

    public ArgumentResolverComposite addResolver(ArgumentResolver... resolvers){
        if (null != resolvers){
            Collections.addAll(this.argumentResolvers, resolvers);
        }
        return this;
    }

    public ArgumentResolverComposite addResolver(List<ArgumentResolver> resolvers){
        if (null != resolvers){
            this.argumentResolvers.addAll(resolvers);
        }
        return this;
    }

    /**
     * 检查该集合中是否有{@link ArgumentResolver}对象可以处理该方法参数
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return CollectionUtils.notEmpty(getArgumentResolvers());
    }

    /**
     * 遍历该集合，找到能够处理该方法参数的resolver并且进行处理
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) {
        return null;
    }


    public List<ArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

    public Map<MethodParameter, ArgumentResolver> getArgumentResolverCache() {
        return argumentResolverCache;
    }
}
