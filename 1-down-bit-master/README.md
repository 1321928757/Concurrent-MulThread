## 案例一、多线程断点续传下载器 - DownBit

### 更新记录
2024-3-12：🔖详细注解补充
2024-2-11：:🔖支持迅雷链接解析下载  
2024-2-10：:🔖优化日志输出方式 🐛修复部分服务器文件不能正常分段下载问题  
2024-1-22：:🐛修复部分文件下载完毕后，续传时日志线程卡死问题  
2023-12-27：:📖多线程断点续传下载初始版本

### 使用方式
```shell
git clone git@github.com:niumoo/down-bit.git
cd down-bit
mvn package
java -jar target/down-bit-jar-with-dependencies.jar 下载链接

```
或者idea直接运行

### 文后语

有帮助可以点「**赞**」在看或 :star: **Star**，谢谢你！


