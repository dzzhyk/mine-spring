package com.yankaizhang.spring.webmvc;

import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.method.support.ArgumentResolverComposite;
import com.yankaizhang.spring.web.method.support.InvocableHandlerMethod;
import com.yankaizhang.spring.web.method.support.ReturnValueResolverComposite;
import com.yankaizhang.spring.web.model.ModelAndView;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.resolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 处理请求传递的参数和controller中方法参数的对应
 * 以及数值转换工作
 * TODO: 这个类目前还是扁平的，如果要拆分各种功能并且提高扩展性，需要改为接口，并且逐层实现
 * @author dzzhyk
 */
public class HandlerAdapter {

    /** 自定义的传入参数处理器 */
    private List<ArgumentResolver> customArgumentResolvers;

    /** 传入参数处理器对象集合类 */
    private final ArgumentResolverComposite argumentResolvers;

    /** 自定义的返回值处理器 */
    private List<ReturnValueResolver> customReturnValueResolvers;

    /** 返回值处理器对象集合类 */
    private final ReturnValueResolverComposite returnValueResolvers;

    public HandlerAdapter() {
        List<ArgumentResolver> defaultArgumentResolvers = getDefaultArgumentResolvers();
        List<ReturnValueResolver> defaultReturnValueResolvers = getDefaultReturnValueResolvers();

        this.argumentResolvers = new ArgumentResolverComposite().addResolver(defaultArgumentResolvers);
        this.returnValueResolvers = new ReturnValueResolverComposite().addResolver(defaultReturnValueResolvers);
    }

    /**
     * 检测是否支持
     */
    public boolean supports(Object handler){
        return (handler instanceof HandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        // handlerMapping是经过封装的handler对象
        HandlerMapping handlerMapping = (HandlerMapping) handler;

        // 创建一个请求包装类WebRequest
        WebRequest webRequest = new WebRequest(req, resp);

        // 拿到需要处理的方法
        InvocableHandlerMethod invocableMethod = new InvocableHandlerMethod(handlerMapping.getMethod());

        // 给这个方法设置当前已知的输入输出解析器
        if (this.argumentResolvers != null) {
            invocableMethod.setArgumentResolvers(this.argumentResolvers);
        }
        if (this.returnValueResolvers != null) {
            invocableMethod.setReturnValueResolvers(this.returnValueResolvers);
        }

        // 返回的mav内容
        ModelAndViewBuilder mavBuilder = new ModelAndViewBuilder();

        // 真正执行方法
        invocableMethod.invokeAndHandle(webRequest, mavBuilder);

        return mavBuilder.build();

//
//        // 保存形参列表
//        HashMap<String, Integer> paramMapping = new HashMap<>();
//        Annotation[][] pa = method.getParameterAnnotations();
//
//        for (int i = 0; i < pa.length; i++) {
//            for (Annotation annotation : pa[i]) {
//                if (annotation instanceof RequestParam){
//                    String paramName = ((RequestParam) annotation).value();
//                    if (!"".equals(paramName.trim())){
//                        // 统计方法的各个含有注解的参数是第几个
//                        paramMapping.put(paramName, i);
//                    }
//                }
//            }
//        }
//
//        // 处理req和resp两个参数
//        Class<?>[] parameterTypes = method.getParameterTypes();
//        for (int i = 0; i < parameterTypes.length; i++) {
//            Class<?> type = parameterTypes[i];
//            if (type == HttpServletRequest.class || type == HttpServletResponse.class){
//                paramMapping.put(type.getName(), i);
//            }
//        }
//
//        // 最后传递给invoke方法执行的参数
//        Object[] paramValues = new Object[parameterTypes.length];
//
//
//        // 请求参数映射
//        Map<String, String[]> params = req.getParameterMap();
//        for (Map.Entry<String, String[]> entry : params.entrySet()) {
//            String value = Arrays.toString(entry.getValue()).replaceAll("[\\[\\]]", "")
//                    .replaceAll("\\s", ",");
//
//            // 如果没有注解标记这个参数，略过
//            if (!paramMapping.containsKey(entry.getKey())) {
//                continue;
//            }
//            // 将传递过来的参数根据名字获得index
//            int index = paramMapping.get(entry.getKey());
//
//            // 根据index获得参数类型，根据这个类型转换param到对应类型
//            paramValues[index] = convert(parameterTypes[index], value);
//        }
//
//        // 如果参数是req或者resp
//        if (paramMapping.containsKey(HttpServletRequest.class.getName())){
//            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
//            paramValues[reqIndex] = req;
//        }
//        if (paramMapping.containsKey(HttpServletResponse.class.getName())){
//            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
//            paramValues[respIndex] = resp;
//        }
//
//        // 处理文件请求
//        if (req instanceof MultipartRequest) {
//            // 如果是文件上传类型
//            MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartRequest) req).getMultiFileMap();
//            for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
//                for (MultipartFile file : entry.getValue()) {
//                    // 如果没有注解标记这个参数，略过
//                    if (!paramMapping.containsKey(entry.getKey())) {
//                        continue;
//                    }
//                    int index = paramMapping.get(entry.getKey());
//                    paramValues[index] = file;
//                }
//            }
//        }
//
//        // 执行方法
//        Object returnValue = method.invoke(handlerMapping.getController(), paramValues);
//        if (returnValue == null) {
//            return null;
//        }
//
//        // 判断返回类型
//        Class<?> returnType = method.getReturnType();
//        ModelAndView modelAndView = null;
//        if (ModelAndView.class.equals(returnType)) {
//            modelAndView = (ModelAndView) returnValue;
//        } else if (String.class.equals(returnType)) {
//            // 如果是String类型
//            String stringValue = (String) returnValue;
//            modelAndView = new ModelAndView(stringValue);
//        } else {
//            return null;
//        }
    }


    /**
     * 将url传过来的String类型转换为多种类型
     */
//    private Object convert(Class<?> type, String value) {
//        if (Integer.class == type){
//            return Integer.valueOf(value);
//        }else if (Double.class == type){
//            return Double.valueOf(value);
//        }else if (int.class == type){
//            return Integer.parseInt(value);
//        }
//        // 可以继续用策略模式增加
//        return value;
//    }


    /**
     * 获取默认的ArgumentResolver列表
     */
    private List<ArgumentResolver> getDefaultArgumentResolvers() {
        List<ArgumentResolver> resolvers = new ArrayList<>();

        // TODO: 在这里添加内置的ArgumentResolver参数处理器
        resolvers.add(new ModelAndViewMethodResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestResponseBodyMethodResolver());
        resolvers.add(new SimpleClassMethodResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());

        return resolvers;
    }

    /**
     * 获取默认的ArgumentResolver列表
     */
    private List<ReturnValueResolver> getDefaultReturnValueResolvers() {
        List<ReturnValueResolver> resolvers = new ArrayList<>();

        // TODO: 在这里添加内置的ReturnValueResolver返回值处理器
        resolvers.add(new ViewNameMethodReturnValueResolver());
        resolvers.add(new ModelAndViewMethodResolver());
        resolvers.add(new RequestResponseBodyMethodResolver());

        return resolvers;
    }
}
