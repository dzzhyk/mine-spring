package com.yankaizhang.spring.web.view;

import com.yankaizhang.spring.web.View;
import com.yankaizhang.spring.webmvc.ModelAndView;
import com.yankaizhang.spring.webmvc.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这是内置的HtmlView类型，目前可以支持简单了el表达式解析，继承自{@link View}
 * @author dzzhyk
 */
public class HtmlView extends AbstractView {

    private static final String EL_PATTERN = "\\$\\{[^}]+}";

    public HtmlView(String url) {
        super(url);
    }

    /**
     * 根据模板和传回的参数渲染
     */
    @Override
    public void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {

        // 获取文件url
        String url = getUrl();

        DispatcherServlet.log.debug("渲染至==>" + url);
        StringBuilder builder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(url), StandardCharsets.UTF_8))
        ) {
            String line = null;
            while (null != (line = br.readLine())) {
                line = new String(line.getBytes(StandardCharsets.UTF_8));
                // 用正则表达式匹配模板中的动态内容 ${abc}
                Pattern pattern = Pattern.compile(EL_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                // 遍历解析所有模板中的动态内容
                while (matcher.find()) {
                    String paramName = matcher.group();
                    // 去掉${ }外壳
                    paramName = paramName.replaceAll("\\$\\{|}", "");
                    Object paramValue = mav.getModel().get(paramName);
                    if (null == paramValue) {
                        continue;
                    }

                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                builder.append(line);
            }
        }
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(builder.toString());
    }



    /**
     * 从字符串创建得到正则字符串
     */
    private String makeStringForRegExp(String str){
        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("|", "\\|").replace("+", "\\+")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }

}
