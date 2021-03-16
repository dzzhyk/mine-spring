package com.yankaizhang.spring.web.method.support;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.CollectionUtils;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ArgumentResolver}对象的一个集合类
 * @author dzzhyk
 * @since 2020-11-28 13:44:38
 */
public class ArgumentResolverComposite implements ArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(ArgumentResolverComposite.class);

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
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {

        ArgumentResolver resolver = selectResolver(parameter);
        if (null == resolver){
            StringBuilder builder = new StringBuilder();
            builder.append("找不到对应的ArgumentResolver => 方法 ").append(parameter.getMethod())
                    .append("，参数位置 ").append(parameter.getParameterIndex())
                    .append("，参数名 ").append(parameter.getParameterName())
                    .append("，不存在这种resolver，或许你需要自定义解析该种类型的ArgumentResolver ?");
            log.warn(builder.toString());
            return null;
        }
        return resolver.resolveArgument(parameter, webRequest);
    }

    private ArgumentResolver selectResolver(MethodParameter parameter) {
        for (ArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(parameter)){
                return resolver;
            }
        }
        return null;
    }


    public List<ArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

    public Map<MethodParameter, ArgumentResolver> getArgumentResolverCache() {
        return argumentResolverCache;
    }
}
