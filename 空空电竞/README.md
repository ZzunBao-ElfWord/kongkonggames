**将lib下的包安装到本地maven仓库**

 mvn install:install-file -Dfile=D:\idea-yuliao\pwiwan-admin\lib\jave-1.0.2.jar -DgroupId=it.sauronsoftware -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar



**技术选型：** 
- 核心框架：Spring Boot 2.1
- 安全框架：Apache Shiro 1.4
- 视图框架：Spring MVC 5.0
- 持久层框架：MyBatis 3.3
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x 
<br> 


 ****
- Swagger文档路径：http://localhost:8080/pwyl-api/swagger/index.html
- Swagger注解路径：http://localhost:8080/pwyl-api/swagger-ui.html