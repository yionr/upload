# 使用SSM制作的作业收集系统

> 由于在线收集作业的过程中,很多同学不按照格式给学委提交作业,导致学委整理十分不方便,特此做了一个小项目

用法：
1. 修改`log4j.properties`和`common.properties`中的两行地址
2. 新建`db.properties`文件，填写如下内容
```properties
spring.datasource.url=jdbc:mysql://yionr.cn:3306/upload?characterEncoding=utf8
spring.datasource.username=xxx
spring.datasource.password=xxx
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
(根据实际情况自己填写！)

完成以上步骤就可以运行了
