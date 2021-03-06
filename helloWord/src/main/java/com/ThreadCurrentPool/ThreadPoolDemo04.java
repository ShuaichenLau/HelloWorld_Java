package com.ThreadCurrentPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的作用
 * <p>
 * 什么是线程池 (线程池目的)
 * Java中的线程池是运用场景最多的并发框架,几乎所有需要异步或者并发执行任务的程序都可以使用线程池
 * 在开发过程中,合理地使用线程池能够带来3个好处
 * 1.降低资源消耗:通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
 * 2.提高响应速度:当任务到达时,任务可以不需要等到线程创建就能立即执行。
 * 3.提高线程的可管理性.线程是稀缺资源,如果无限制的创建,不仅会消耗系统资源,还会降低系统的稳定性,使用线程池可以进行统一分配、调优和监控。但是，要做到合理利用。
 * <p>
 * 线程池，必须对其实现原理了如指掌。
 * <p>
 * 线程池溢出,频繁的创建多线程,非常占用CPU
 * <p>
 * !!!重点: 如果配置合理的线程数!!!
 * <p>
 * <p>
 * 自定义线程池
 * 如果当前线程池中的线程数目小于corePoolSize，则没来一个任务，就会创建一个线程去执行这个任务;
 * 如果当前线程池中的线程数目>=corePoolSize,则每来一个任务,会尝试将其添加到任务缓存队列当中,
 * 若添加成功,则该任务会等待空闲线程将其取出去执行;若添加失败(一般来说是任务缓存队列已经满了),
 * 则会尝试创建新的线程去执行这个任务;
 * <p>
 * 如果队列满了,则在总线程数不大于maximumPoolSize的前提下,则会创建新的线程
 * 如果当前线程池中的线程书库达到maximumPoolSize,则会采取任务拒绝策略进行处理
 * <p>
 * 如果线程池中的线程数量大于corePoolSize时,如果某线程空闲时间超过keepAliveTime,线程将被终止,
 * 直到线程池中的线程数目不大于corePoolSize;如果允许为核心池中的线程设置存活时间,那么核心池中的线程空闲时间超过keepAliveTime,线程也会被终止
 *
 *
 *
 * 如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
 * 如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
 * 如果队列已经满了，则在总线程数不大于maximumPoolSize的前提下，则创建新的线程
 * 如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
 * 如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。
 *
 * newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 * newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
 * newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 *
 */

class TaskThread implements Runnable {

    public TaskThread() {
    }

    private String threadName;

    public TaskThread(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println("threadName==>" + threadName + ",线程是==>" + Thread.currentThread().getName());
    }
}


public class ThreadPoolDemo04 {

    public static void main(String[] args) {

        // 线程池的含义 核心数 最大线程数 存活时间
        // corePoolSize 核心线程数 表示最大可运行的线程数  核心线程数只能 <= maximumPoolSize最大可创建的线程数
        // maximumPoolSize 表示可以最多创建线程数
        ThreadPoolExecutor threadPoolExecutor
                = new ThreadPoolExecutor(1, 4, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(6));

        try {
            // 任务1 在创建线程 在执行
            threadPoolExecutor.execute(new TaskThread("task1"));
            // 任务2 存放在队列缓存
            threadPoolExecutor.execute(new TaskThread("task2"));
            // 任务3 存放在队列缓存
            threadPoolExecutor.execute(new TaskThread("task3"));
            threadPoolExecutor.execute(new TaskThread("task4"));
            threadPoolExecutor.execute(new TaskThread("task5"));
            threadPoolExecutor.execute(new TaskThread("task6"));
            threadPoolExecutor.execute(new TaskThread("task7"));
            threadPoolExecutor.execute(new TaskThread("task8"));
            threadPoolExecutor.execute(new TaskThread("task9"));
            threadPoolExecutor.execute(new TaskThread("task10"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(" maximumPoolSize + capacity ");
            System.out.println(" threadPoolExecutor.getPoolSize() ==> " + threadPoolExecutor.getPoolSize());
            //关闭线程池
            threadPoolExecutor.shutdown();
        }

        /**
         * 创建线程任务6就会报错
         * Exception in thread "main" java.util.concurrent.RejectedExecutionException: Task com.ThreadCurrentPool.TaskThread@4f023edb rejected from java.util.concurrent.ThreadPoolExecutor@3a71f4dd[Running, pool size = 2, active threads = 2, queued tasks = 3, completed tasks = 0]
         * 	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:2063)
         * 	at java.util.concurrent.ThreadPoolExecutor.reject(ThreadPoolExecutor.java:830)
         * 	at java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1379)
         * 	at com.ThreadCurrentPool.ThreadPoolDemo04.main(ThreadPoolDemo04.java:72)
         * 	threadPoolExecutor.execute(new TaskThread("task6"));
         */


    }

}
