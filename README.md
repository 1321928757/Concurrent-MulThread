## 本仓库主要分享一些Java多线程编程的实用代码片段 -by LuckySJ@刘仕杰

### 代码文章讲解(CSDN)
案例五、从零手搓一个简易线程池  
[Java多线程实战-从零手搓一个简易线程池(一)定义任务等待队列](https://blog.csdn.net/qq_35716689/article/details/136962958)  
[Java多线程实战-从零手搓一个简易线程池(二)线程池与拒绝策略实现](https://blog.csdn.net/qq_35716689/article/details/137072055)  
[Java多线程实战-从零手搓一个简易线程池(三)线程工厂，核心线程与非核心线程逻辑实现](https://blog.csdn.net/qq_35716689/article/details/137123379)   
[Java多线程实战-从零手搓一个简易线程池(四)线程池生命周期状态流转实现](https://blog.csdn.net/qq_35716689/article/details/137541593)

案例四，多线程编程优化查询接口响应速度  
[Java多线程实战-CompletableFuture异步编程优化查询接口响应速度](https://blog.csdn.net/qq_35716689/article/details/136868259)

案例三，CountDownLatch模拟压测实现  
[Java多线程实战-CountDownLatch模拟压测实现](https://blog.csdn.net/qq_35716689/article/details/136789433)

案例二，多线程异步保存日志  
[Java多线程实战-异步操作日志记录解决方案(AOP+注解+多线程)](https://blog.csdn.net/qq_35716689/article/details/136748521)

案例一，多线程下载器  
[Java多线程实战-实现多线程文件下载，支持断点续传、日志记录等功能](https://blog.csdn.net/qq_35716689/article/details/136597588)


### 更新记录
案例五、从零手搓一个简易线程池  
2024-4-9：✨ 手搓简易线程池系列完结  
2024-4-9：🔖 实现线程池的生命周期管理(分支liushijie-240409-lifecycle)  
2024-4-2：🔖 实现核心线程与非核心线程逻辑(分支liushijie-240329-core)  
2024-3-29：🔖 添加当前线程数字段，部分代码调整  
2024-3-28：🔖 完成线程工厂的设计与实现(分支liushijie-240328-factory)  
2024-3-27：📖 完成线程池的定义，支持拒绝策略 初始版本  
2024-3-25：🖌定义任务阻塞队列BlockQueue，支持堵塞读写任务与带超时的堵塞读写任务

案例四，多线程编程优化查询接口响应速度  
2024-3-19：📖CompletableFuture异步编程优化并发查询接口响应速度 初始版本

案例三，CountDownLatch模拟压测实现  
2024-3-17：📖CountDownLatch模拟压测代码片段 初始版本

案例二，多线程异步保存日志  
2024-3-15：📖 多线程异步记录操作 初始版本

案例一，多线程下载器  
2024-3-12：🔖详细注解补充  
2024-2-11：🔖支持迅雷链接解析下载  
2024-2-11：🔖支持迅雷链接解析下载  
2024-2-10：🔖优化日志输出方式 🐛 修复部分服务器文件不能正常分段下载问题  
2024-2-9： 🐛修复部分文件下载完毕后，续传时日志线程卡死问题  
2023-2-3：📖 多线程断点续传下载 初始版本

### 文后语

有帮助可以点「**赞**」在看或 :star: **Star**，谢谢你！

