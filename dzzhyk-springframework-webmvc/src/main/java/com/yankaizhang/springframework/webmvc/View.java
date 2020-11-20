package com.yankaizhang.springframework.webmvc;

import com.yankaizhang.springframework.webmvc.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板解析引擎
 * @author dzzhyk
 */
public class View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String EL_PATTERN = "\\$\\{[^}]+}";
    private static final String JSP_SUFFIX = ".jsp";

    private final File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    /**
     * 根据模板和传回的参数渲染
     */
    public void render(ModelAndView mav, ViewResolver viewResolver,
                       HttpServletRequest req, HttpServletResponse resp) throws Exception{

        // 如果是jsp
        if (JSP_SUFFIX.equals(viewResolver.getSuffix())){
            // 拼接Jsp文件路径，然后转发
            String path = viewResolver.getPrefix() + mav.getViewName() + viewResolver.getSuffix();
            req.getRequestDispatcher(path).forward(req, resp);
            return;
        }

        // 如果不是jsp，目前可以简单解析模板，支持el表达式
        DispatcherServlet.log.debug("渲染至==>" + viewFile.getName());
        StringBuilder builder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(viewFile), StandardCharsets.UTF_8))
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
