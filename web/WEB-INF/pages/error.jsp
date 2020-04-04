<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>error</title>
</head>
<body>
<h1 style="text-align: center;color: red">文件提交过程中出现了问题</h1>
<h3>以下是问题描述:</h3>
<hr/>
${msg}
<hr/>
<h3>请将以上信息反馈给管理员,将在下个版本得到解决</h3>
<p>
    ps.如果要重新提交作业，请暂时先QQ发给我，将在后续更新完善
</p>
<%--<a href="https://upload.yionr.cloud">返回</a>--%>
<a href="http://localhost:8080/upload_war_exploded/">返回</a>
</body>
</html>
