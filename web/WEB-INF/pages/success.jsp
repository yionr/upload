<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
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
        a{
            display: block;
            text-align: center;
            font-size: 3rem;
        }
    </style>
</head>
<body ondragstart="return false">
<div>
    <img src="img/success.png" alt="" width="400px" height="400px">
</div>
<p>上传成功</p>
<a href="http://localhost:8080/upload_war">点此返回</a>
</body>
</html>
