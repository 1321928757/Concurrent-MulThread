package com.luckysj.threadpool.factory;

/**
 * @author Luckysj @刘仕杰
 * @description 线程工厂接口
 * @create 2024/03/28 20:40:18
 */
public interface ThreadFactory {
    /**
    * @description
    * @param 
    * @return 创建的线程对象
    * @date 2024/03/28 21:01:35
    */
    Thread newThread(Runnable r);
}
