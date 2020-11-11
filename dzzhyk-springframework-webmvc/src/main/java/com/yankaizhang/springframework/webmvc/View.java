package com.yankaizhang.springframework.webmvc;

import com.yankaizhang.springframework.webmvc.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板解析引擎
 * @author dzzhyk
 */
public class View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public static String getDefaultContentType() {
        return DEFAULT_CONTENT_TYPE;
    }

    /**
     * 根据模板和传回的参数渲染
     */
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception{
        StringBuffer buffer = new StringBuffer();

        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(viewFile), StandardCharsets.UTF_8));

        try {
            String line = null;
            while (null != (line = br.readLine())) {
                line = new String(line.getBytes(StandardCharsets.UTF_8));
                // 用正则表达式匹配模板中的动态内容 ${abc}
                Pattern pattern = Pattern.compile("\\$\\{[^}]+}", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);

                // 遍历解析所有模板中的动态内容
                while (matcher.find()) {
                    String paramName = matcher.group();
                    // 去掉${ }外壳
                    paramName = paramName.replaceAll("\\$\\{|}", "");
                    Object paramValue = model.get(paramName);
                    if (null == paramValue) continue;

                    line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                    matcher = pattern.matcher(line);
                }
                buffer.append(line);
            }
        } finally {
            br.close();
        }

        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(buffer.toString());
        DispatcherServlet.logger.debug("渲染至==>" + viewFile.getName());
    }

    public static String makeStringForRegExp(String str){
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
