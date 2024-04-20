package com.luckysj.synchronizer;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author Luckysj @刘仕杰
 * @description 基于AQS实现的互斥锁类
 * @create 2024/04/20 20:44:07
 */
public class OnlySyncByAQS {

    private final Sync sync = new Sync();

    /**
     * 获取许可，给资源上锁
     */
    public void lock() {
        sync.acquire(1);
    }

    /**
     * 释放许可，解锁
     */
    public void unlock() {
        sync.release(1);
    }

    /**
     * 判断是否独占
     * @return
     */
    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    /**
     * 静态内部类，继承AQS，重写钩子方法
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 重写AQS的tryAcquire方法，独占方式，尝试获取资源。
         */
        @Override
        protected boolean tryAcquire(int arg) {
            //CAS 尝试更改状态
            if (compareAndSetState(0, 1)) {
                //独占模式下，设置锁的持有者为当前线程，来自于AOS
                setExclusiveOwnerThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName()+"获取锁成功");
                return true;
            }
            System.out.println(Thread.currentThread().getName()+"获取锁失败");
            return false;
        }

        /**
         * 独占方式。尝试释放资源，成功则返回true，失败则返回false。
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            //置空锁的持有者
            setExclusiveOwnerThread(null);
            //改状态为0，未锁定状态
            setState(0);
            System.out.println(Thread.currentThread().getName()+"释放锁成功！");
            return true;
        }

        /**
         * 判断该线程是否正在独占资源，返回state=1
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }

}
