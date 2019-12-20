<%--
  Created by IntelliJ IDEA.
  User: yionr
  Date: 2019/11/14
  Time: 21:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传成功</title>
    <style>
        *{
            margin: 0;
            padding: 0;
        }
        html,body{
            width: 100%;
            height: 100%;
        }
        body{
            user-select: none;
            overflow:hidden;
        }
        div{
            display: flex;
            justify-content: center;
        }
        p{
            text-align: center;
            font-size: 6rem;
        }
    </style>
</head>
<body ondragstart="return false">
<div>
    <img src="img/success.png" alt="">
</div>
<p>上传成功</p>
</body>
</html>
