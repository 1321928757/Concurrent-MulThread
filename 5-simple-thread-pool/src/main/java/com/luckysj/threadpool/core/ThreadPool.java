package com.luckysj.threadpool.core;

import com.luckysj.threadpool.factory.ThreadFactory;
import com.luckysj.threadpool.policy.RejectPolicy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Luckysj @刘仕杰
 * @description 自定义线程池对象
 * @create 2024/03/27 10:45:17
 */
@Slf4j
public class ThreadPool {
    /** 任务等待队列 */
    private WorkQueue<Runnable> workQueue;
    /** 正在运行的工作线程集合 */
    private final Set<Worker> workerSet = new HashSet<>();
    /** 核心线程数 */
    private int corePoolSize;
    /** 最大线程数 */
    private int maximumPoolSize;
    /** 最大等待时间（也就是线程的最大空闲时间) */
    private Long keepAliveTime;
    /** 等待时间单位 */
    private TimeUnit timeUnit;
    /** 拒绝策略 */
    private RejectPolicy<Runnable> rejectPolicy;
    /** 线程工厂 */
    private ThreadFactory threadFactory;
    /** 当前线程中的线程数 */
    private final AtomicInteger threadTotalNums = new AtomicInteger(0);
    /** 是否允许核心线程被回收 */
    private boolean allowCoreThreadTimeOut;

    /** 线程池状态常量*/
    private static final int RUNNING    = 1;
    private static final int SHUTDOWN   = 2;
    private static final int STOP       = 3;
    private static final int TIDYING    = 4;
    private static final int TERMINATED = 5;

    /** 线程池当前状态*/
    private final AtomicInteger state = new AtomicInteger(RUNNING);

    public ThreadPool(WorkQueue<Runnable> taskQueue, int corePoolSize, int maximumPoolSize, Long keepAliveTime, TimeUnit timeUnit, RejectPolicy<Runnable> rejectPolicy, ThreadFactory threadFactory) {
        this.workQueue = taskQueue;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
        this.threadFactory = threadFactory;
        this.allowCoreThreadTimeOut = false;
        log.info("\n---------------线程池创建成功--------------\n" +
                "最大核心线程数：{}\n" +
                "最大总线程数：{}\n" +
                "线程最大空闲时间：{}\n" +
                "空闲时间单位：{}\n" +
                "allowCoreThreadTimeOut：{}\n" +
                "----------------------------------------",
                corePoolSize, maximumPoolSize,keepAliveTime, timeUnit.toString(), allowCoreThreadTimeOut);
    }

    public WorkQueue<Runnable> getQueue() {
        return workQueue;
    }

    // 为了方便使用，我们封装工作线程
    class Worker implements Runnable{
        private Runnable firstTask;

        private Thread thread;

        public Worker(Runnable task) {

            this.firstTask = task;
            this.thread = threadFactory.newThread(this);
        }

        @Override
        public void run() {
            log.info("工作线程====》工作线程{}开始运行", Thread.currentThread());

            // 1。首先消费当前任务，消费完再去任务队列取，while循环实现线程复用
            while(true){
                if (!(firstTask != null || (firstTask = getTask()) != null)) break;
                try {
                    firstTask.run();
                }catch (Exception e){
                    log.error("执行任务{}时发生了异常{}", firstTask.toString(), e.getMessage());
                }finally {
                    // 执行完后清除任务
                    firstTask = null;
                }
            }

            // 2.跳出循环，说明取任务超过了最大等待时间，线程歇菜休息吧
            synchronized (workerSet){
                workerSet.remove(this);
                threadTotalNums.decrementAndGet(); //计数扣减
            }
            log.info("工作线程====》线程{}已被回收，当前线程数:{}", Thread.currentThread(), threadTotalNums.get());
            tryTerminate(); // 尝试转换线程池状态
        }
    }

    public void execute(Runnable task){
        if(task == null){
            throw new NullPointerException("传递的Runnable任务为Null");
        }
        // 1.如果线程池状态为SHUTDOWN以上，不再接受任务，直接触发拒绝策略
        if(isShutdown()){
            reject(task);
        }

        // 2.如果当前线程数小于核心线程,直接创建线程去运行
        if(threadTotalNums.get() < corePoolSize){
            if(addWorker(task, true)) return;
        }

        // 3.线程数大于核心线程，我们就将任务加入等待队列
        if(workQueue.offer(task)){
            return;
        }
        // 4.队列满了，尝试创建非核心线程，如果失败就触发拒绝策略
        else if(!addWorker(task, false)){
            reject(task);
        }

    }

    /**
    * @description 触发拒绝策略
    * @param task 被拒绝的任务
    * @date 2024/04/02 13:12:08
    */
    private void reject(Runnable task) {
        rejectPolicy.reject(this, task);
    }

    public void setAllowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut){
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }
    /**
    * @description 添加工作线程
    * @param firstTask 线程第一次执行的任务
    * @param isCore 是否为核心线程
    * @return Boolean 线程是否添加成功
    * @date 2024/04/02 10:42:43
    */
    public Boolean addWorker(Runnable firstTask, Boolean isCore){
        if(firstTask == null) {
            throw new NullPointerException();
        }
        // 1.生命周期检查，不在RUNNING状态下不继续添加工作线程（但是可能存在刚关闭线程池的情况，此时状态为shutdown，如果任务队列不为空，我们依旧允许创建线程，来加快任务处理）
        final int c = state.get();
        if (c > RUNNING && !(c == SHUTDOWN && !workQueue.isEmpty())) {
            return false;
        }

        // 2.根据当前线程池和isCore条件判断是否需要创建
        int wc = threadTotalNums.get();
        if (wc >= (isCore ? corePoolSize : maximumPoolSize))
            return false;
        // 3.创建线程，并添加到线程集合中
        Worker worker = new Worker(firstTask);
        Thread t = worker.thread;
        if(t != null){
            synchronized (workerSet){
                workerSet.add(worker);
                threadTotalNums.getAndIncrement();
            }
            t.start();
            return true;
        }
        return false;
    }

    /**
    * @description 从等待队列中获取任务
    * @return Runnable 待执行的任务，没有获取到会返回null
    * @date 2024/04/02 10:46:37
    */
    public Runnable getTask()  {
        //我们使用一个变量来记录上次循环获取任务是否超时
        boolean preIsTimeOut = false;
        // 内部使用一个while循环，线程会一直尝试获取任务，直到成功获取到任务或者满足退出条件
        while(true){
            // 1.线程池状态判断，如果为关闭状态并且任务队列为空，就返回null
            int rs = state.get();
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                return null;
            }

            // 2.根据情况动态调整线程数
            int wc = threadTotalNums.get();
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
            if ( (wc > maximumPoolSize || (timed && preIsTimeOut)) && (wc > 1 || workQueue.isEmpty()) ) {
                return null;
            }

            // 3.根据timed这个条件来选择是超时堵塞
            Runnable r = null;
            try {
                r = timed ?
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                if (r != null)
                    return r;
                // 获取任务超时了，将preIsTimeOut设为true，下次可以执行回收
                preIsTimeOut = true;
            } catch (InterruptedException e) {
                // 检测到InterruptedException，也就是中断触发，我们直接返回null让线程回收即可
                return null;
            }
        }
    }

    /**
     * @description 关闭线程池，这种方式不会中断正在运行的线程
     * @date 2024/04/09 14:32:11
     */
    public void shutdown() {
        // 如果为
        if (state.compareAndSet(RUNNING, SHUTDOWN)) {
            log.info("线程池正在关闭");
            tryTerminate(); // 尝试转换到TERMINATED状态
        }
    }

    /**
    * @description 立刻关闭线程池
    * @date 2024/04/09 14:32:11
    */
    public void shutdownNow() {
        if (state.compareAndSet(RUNNING, STOP)) {
            try {
                log.info("线程池立即关闭，尝试中断所有线程");
                // 中断所有正在运行的线程
                synchronized (workerSet){
                    for (Worker worker : workerSet) {
                        worker.thread.interrupt();
                    }
                }
            } finally {
                state.set(TIDYING);
                // 在此处执行清理工作
                transitionToTerminated();
                log.info("线程池已终止");
            }
        }
    }

    private void tryTerminate() {
        if ((state.get() == SHUTDOWN || state.get() == STOP) && workQueue.isEmpty() && workerSet.isEmpty()) {
            if (state.compareAndSet(SHUTDOWN, TIDYING) || state.compareAndSet(STOP, TIDYING)) {
                // 在此处执行清理工作
                transitionToTerminated();
                log.info("线程池已终止");
            }
        }
    }

    private void transitionToTerminated() {
        state.set(TERMINATED);
        // 这里可以通知等待线程池终止的线程

    }

    public boolean isShutdown() {
        return state.get() >= SHUTDOWN;
    }

    public boolean isTerminated() {
        return state.get() == TERMINATED;
    }
}
