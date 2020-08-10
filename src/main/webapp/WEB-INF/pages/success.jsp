<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <meta charset="UTF-8">
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

        .pic{
            display: inline-block;
            position: relative;
            left: calc(50% - 30vh);
            height: 60%;
            width: 60vh;
        }
        img{
            width: 100%;
            height: 100%;
            max-width: 100%;
            max-height: 100%;
        }
        p{
            text-align: center;
            font-size: 10vh;
        }
        .return{
            position: fixed;
            bottom: 0;
            font-size: 7vh;
            left: calc(50% - 14vh);
        }
    </style>
</head>
<body>
<div class="pic">
    <img src="img/success.png" alt="">
</div>
<p>上传成功</p>
<div class="return">
    <a href="#" id="ret">点此返回</a>
</div>



<script>
    history.pushState(null, '', 'temp.html')
    let a = document.getElementById('ret');
    let mainPage = localStorage.getItem('mainPage')
    a.onclick = function(){
        location.href = mainPage
    }
</script>
</body>
</html>