FROM tomcat:7
MAINTAINER yionr <yionr99@gmail.com>
COPY target/upload-0.0.1.war /usr/local/tomcat/webapps/ROOT.war
# 映射端口
EXPOSE 8080

# 容器启动时执行指令
# 容器启动时启动tomcat并打印日志
ENTRYPOINT /usr/local/tomcat/bin/startup.sh && tail -f /usr/local/tomcat/logs/catalina.out