package com.yankaizhang.spring.web.method.support;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.CollectionUtils;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link ReturnValueResolver}对象的一个集合类
 * @author dzzhyk
 */
public class ReturnValueResolverComposite implements ReturnValueResolver {

    /** 返回值解析器列表 */
    private final List<ReturnValueResolver> returnValueResolvers = new ArrayList<>();

    public ReturnValueResolverComposite addResolver(ReturnValueResolver resolver){
        this.returnValueResolvers.add(resolver);
        return this;
    }

    public ReturnValueResolverComposite addResolver(ReturnValueResolver... resolvers){
        if (null != resolvers){
            Collections.addAll(this.returnValueResolvers, resolvers);
        }
        return this;
    }

    public ReturnValueResolverComposite addResolver(List<ReturnValueResolver> resolvers){
        if (null != resolvers){
            this.returnValueResolvers.addAll(resolvers);
        }
        return this;
    }

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return CollectionUtils.notEmpty(getReturnValueResolvers());
    }

    @Override
    public void resolveReturnValue(Object returnValue, MethodParameter returnType,
                                   ModelAndViewBuilder mav, WebRequest request) throws Exception {
        ReturnValueResolver handler = selectHandler(returnValue, returnType);
        if (handler == null){
            throw new Exception("找不到对应的ReturnValueResolver => " + returnType);
        }
        handler.resolveReturnValue(returnValue, returnType, mav, request);
    }

    /**
     * 根据返回值和返回类型选择相应的返回值处理器
     */
    private ReturnValueResolver selectHandler(Object returnValue, MethodParameter returnType) {

        for (ReturnValueResolver resolver : this.returnValueResolvers) {
            if (resolver.supportsReturnType(returnType)){
                return resolver;
            }
        }
        return null;
    }

    public List<ReturnValueResolver> getReturnValueResolvers() {
        return returnValueResolvers;
    }
}
