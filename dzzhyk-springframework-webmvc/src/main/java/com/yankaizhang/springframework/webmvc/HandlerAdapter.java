package com.yankaizhang.springframework.webmvc;


import com.yankaizhang.springframework.util.MultiValueMap;
import com.yankaizhang.springframework.webmvc.annotation.RequestParam;
import com.yankaizhang.springframework.webmvc.multipart.MultipartFile;
import com.yankaizhang.springframework.webmvc.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 处理请求传递的参数和controller中方法参数的对应
 * 以及数值转换工作
 * @author dzzhyk
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

        Object[] paramValues = new Object[parameterTypes.length];   // 最后传递给invoke方法执行的参数


        // 请求参数映射
        Map<String, String[]> params = req.getParameterMap();
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

        // 处理文件请求
        if (req instanceof MultipartRequest) {
            // 如果是文件上传类型
            MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartRequest) req).getMultiFileMap();
            for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
                for (MultipartFile file : entry.getValue()) {
                    if (!paramMapping.containsKey(entry.getKey())) continue;   // 如果没有注解标记这个参数，略过
                    int index = paramMapping.get(entry.getKey());
                    paramValues[index] = file;
                }
            }
        }


        // 执行方法
        Object returnValue = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (returnValue == null) return null;


        // 判断返回类型
        Class<?> returnType = handlerMapping.getMethod().getReturnType();
        ModelAndView modelAndView = null;
        if (ModelAndView.class.equals(returnType)) {
            modelAndView = (ModelAndView) returnValue;
        } else if (String.class.equals(returnType)) {
            modelAndView = new ModelAndView((String) returnValue);
        } else {
            return null;
        }
        // 将request中的内容加入model
        return parseRequestAttributes(modelAndView, req);
    }


    /**
     * 解析request中的attribute，将其加入model
     */
    private ModelAndView parseRequestAttributes(ModelAndView modelAndView, HttpServletRequest request){
        Enumeration attributeNames = request.getAttributeNames();
        Map<String, Object> model = (Map<String, Object>) modelAndView.getModel();
        if (null == model){
            model = new HashMap<>();
        }
        while (attributeNames.hasMoreElements()){
            String s = (String) attributeNames.nextElement();
            Object attribute = request.getAttribute(s);
            model.put(s, attribute);
        }
        modelAndView.setModel(model);
        return modelAndView;
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
