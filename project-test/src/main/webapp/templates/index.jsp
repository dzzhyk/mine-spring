<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Map" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">　
    <title>主页</title>
</head>
<body>
<h1>MVC Web演示主页</h1>
<h2>${msg}</h2>

<form>
    <input type="file" name="file">
    <button type="submit">上传</button>
</form>

<h2>文件列表</h2>
<ul>
    <%
        Map<String, String> fileMap = (Map<String, String>) request.getAttribute("fileMap");
        if (fileMap != null){
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
    %>
    <li>源文件名：<%=entry.getKey() %>，服务器文件名：<%=entry.getValue() %></li>
    <%
            }
        }
    %>
</ul>

</body>
<script src="js/test.js"></script>
</html>
