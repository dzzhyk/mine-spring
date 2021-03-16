<%--
  Created by IntelliJ IDEA.
  User: dzzhyk
  Date: 2021/3/16
  Time: 4:59 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/my.css">
</head>
<body>

    <h1>${msg}</h1>

    <form method="post" action="/upload" enctype="multipart/form-data">
        <input type="file" name="file">
        <button type="submit">提交</button>
    </form>

</body>
</html>
