# 使用SSM制作的作业收集系统

> 由于在线收集作业的过程中,很多同学不按照格式给学委提交作业,导致学委整理十分不方便,特此做了一个小项目

# 使用方法

## 面向用户
用户将文件拖拽至上传框，即可完成上传，如果后台无法根据文件名判断这个文件的所属，会弹框要求用户手动输入学号和姓名，上传成功之后可以在下面的服务器已上传列表看到自己的作业

## 面向管理者
在服务器的指定位置直接一次性提取指定周次所有已上传的命名格式统一的作业，作业会根据周次的不同自动划分文件夹


# 部署方法
- 在src/main/resources 下新建一个`db.properties`，内容为以下:

```properties

jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://{ip}/{database}?useUnicode=true&characterEncoding=utf8
jdbc.username={username}
jdbc.password={password}
# 将带`{}`的内容换为自己的相应信息即可

```

- 数据库设计： 两个字段 `varchar id primarykey` `varchar name`

- 修改success.jsp 和 error.jsp把地址改为当前项目的网址

- common.properties中将homeWorkRoot修改为作业保存的地址
