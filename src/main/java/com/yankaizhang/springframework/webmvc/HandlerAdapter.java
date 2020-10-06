package com.yankaizhang.springframework.webmvc;

import com.yankaizhang.springframework.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理请求传递的参数和controller中方法参数的对应
 * 以及数值转换工作
 */
public class HandlerAdapter {

    /**
     * 检测是否支持
     */
    public boolean supports(Object handler){
        return (handler instanceof HandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        // handlerMapping是经过封装的handler对象
        HandlerMapping handlerMapping = (HandlerMapping) handler;

        // 保存形参列表
        HashMap<String, Integer> paramMapping = new HashMap<>();
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();

        for (int i = 0; i < pa.length; i++) {
            for (Annotation annotation : pa[i]) {
                if (annotation instanceof RequestParam){
                    String paramName = ((RequestParam) annotation).value();
                    if (!"".equals(paramName.trim())){
                        paramMapping.put(paramName, i); // 统计方法的各个含有注解的参数是第几个
                    }
                }
            }
        }

        // 处理req和resp两个参数
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramMapping.put(type.getName(), i);
            }
        }

        // 请求参数映射
        Map<String, String[]> params = req.getParameterMap();
        Object[] paramValues = new Object[parameterTypes.length];   // 最后传递给invoke方法执行的参数

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String value = Arrays.toString(entry.getValue()).replaceAll("[\\[\\]]", "")
                    .replaceAll("\\s", ",");
            if (!paramMapping.containsKey(entry.getKey())) continue;   // 如果没有注解标记这个参数，略过
            // 将传递过来的参数根据名字获得index
            int index = paramMapping.get(entry.getKey());

            // 根据index获得参数类型，根据这个类型转换param到对应类型
            paramValues[index] = convert(parameterTypes[index], value);
        }

        // 如果参数是req或者resp
        if (paramMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if (paramMapping.containsKey(HttpServletResponse.class.getName())){
            int respIndex = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = resp;
        }

        Object returnValue = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (returnValue == null) return null;

        // 判断返回类型是否是ModelAndView类型
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if (isModelAndView){
            return (ModelAndView) returnValue;
        }else {
            // 如果不是返回ModelAndView，返回Null
            return null;
        }
    }


    /**
     * 将url传过来的String类型转换为多种类型
     */
    private Object convert(Class<?> type, String value) {
        if (Integer.class == type){
            return Integer.valueOf(value);
        }else if (Double.class == type){
            return Double.valueOf(value);
        }else if (int.class == type){
            return Integer.parseInt(value);
        }
        // 可以继续用策略模式增加
        return value;
    }

}
