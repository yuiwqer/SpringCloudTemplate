# 准备环境
## java装好 推荐版本8
## mysql装好 推荐版本8
## test.sql文件导入到数据库 数据库名为test
## 安装\启动\配置nacos
###### nacos快速开始地址
https://nacos.io/zh-cn/docs/quick-start.html
找到适合的nacos版本并启动
nacos启动文件位置为nacos\bin\startup.cmd
加入配置
配置名为user-service
内容为
```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```
## 启动用户服务(user-service)
## 启动路由服务(gateway)
## 使用Postman调试
```
使用post请求
请求路径为localhost:1002/user-service/login
Body参数类型 application/x-www-form-urlencoded
username | sui
password | 123
```
## 测试
登陆成功
