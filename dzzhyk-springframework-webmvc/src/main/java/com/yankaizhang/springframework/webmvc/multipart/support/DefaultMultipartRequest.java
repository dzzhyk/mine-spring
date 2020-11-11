package com.yankaizhang.springframework.webmvc.multipart.support;

import com.yankaizhang.springframework.util.MultiValueMap;
import com.yankaizhang.springframework.webmvc.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * MultipartRequest接口的默认实现
 * @author dzzhyk
 */
public class DefaultMultipartRequest extends AbstractMultipartRequest {

    private Map<String, String[]> multipartParameters;

    private Map<String, String> multipartParameterContentTypes;


    /**
     * Wrap the given HttpServletRequest in a MultipartHttpServletRequest.
     * @param request the servlet request to wrap
     * @param mpFiles a map of the multipart files
     * @param mpParams a map of the parameters to expose,
     * with Strings as keys and String arrays as values
     */
    public DefaultMultipartRequest(HttpServletRequest request, MultiValueMap<String, MultipartFile> mpFiles,
                                   Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes) {

        super(request);
        setMultipartFiles(mpFiles);
        setMultipartParameters(mpParams);
        setMultipartParameterContentTypes(mpParamContentTypes);
    }

    /**
     * Wrap the given HttpServletRequest in a MultipartHttpServletRequest.
     * @param request the servlet request to wrap
     */
    public DefaultMultipartRequest(HttpServletRequest request) {
        super(request);
    }


    @Override
    public String getParameter(String name) {
        String[] values = getMultipartParameters().get(name);
        if (values != null) {
            return (values.length > 0 ? values[0] : null);
        }
        return super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        String[] mpValues = getMultipartParameters().get(name);
        if (mpValues == null) {
            return parameterValues;
        }
        if (parameterValues == null || getQueryString() == null) {
            return mpValues;
        }
        else {
            String[] result = new String[mpValues.length + parameterValues.length];
            System.arraycopy(mpValues, 0, result, 0, mpValues.length);
            System.arraycopy(parameterValues, 0, result, mpValues.length, parameterValues.length);
            return result;
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Map<String, String[]> multipartParameters = getMultipartParameters();
        if (multipartParameters.isEmpty()) {
            return super.getParameterNames();
        }

        Set<String> paramNames = new LinkedHashSet<>();
        paramNames.addAll(Collections.list(super.getParameterNames()));
        paramNames.addAll(multipartParameters.keySet());
        return Collections.enumeration(paramNames);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new LinkedHashMap<>();
        Enumeration<String> names = getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            result.put(name, getParameterValues(name));
        }
        return result;
    }

    public String getMultipartContentType(String paramOrFileName) {
        MultipartFile file = getFile(paramOrFileName);
        if (file != null) {
            return file.getContentType();
        }
        else {
            return getMultipartParameterContentTypes().get(paramOrFileName);
        }
    }

    protected final void setMultipartParameters(Map<String, String[]> multipartParameters) {
        this.multipartParameters = multipartParameters;
    }

    protected Map<String, String[]> getMultipartParameters() {
        return this.multipartParameters;
    }

    protected final void setMultipartParameterContentTypes(Map<String, String> multipartParameterContentTypes) {
        this.multipartParameterContentTypes = multipartParameterContentTypes;
    }

    protected Map<String, String> getMultipartParameterContentTypes() {
        return this.multipartParameterContentTypes;
    }
}
