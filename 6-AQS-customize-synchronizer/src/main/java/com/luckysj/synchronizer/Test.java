package com.luckysj.synchronizer;

import java.util.concurrent.Semaphore;

public class Test {

    private OnlySyncByAQS onlySyncByAQS = new OnlySyncByAQS();

    public void use(){
        onlySyncByAQS.lock();
        try {
            //休眠1秒获取使用共享资源
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            onlySyncByAQS.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        //多线程竞争资源，每次仅一个线程拿到锁
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                test.use();
            }).start();
        }

    }
}
