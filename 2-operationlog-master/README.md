## 案例二、多线程异步记录操作日志 - OperationLog

### 更新记录
2024-3-15：:📖多线程异步记录操作demo发布

### 使用方式
1.导入src/main/resources/db/data-h2.sql数据库文件

2.修改yml数据库配置文件

3.启动项目

4.访问测试

```java
Post http://localhost:8080/user/add
{
    "userName":"张三",
    "userPhone":"12312312"
}
```

5.查看库中日志

### 文后语

有帮助可以点「**赞**」在看或 :star: **Star**，谢谢你！
