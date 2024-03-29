package com.github.cuinipeng.utils;

/*
 * http://www.importnew.com/29813.html
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class ThreadPoolUsage {
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolUsage.class);

    public void run() {
        logger.info("ThreadPoolUsage.run()");
        useThreadPool();
    }

    public void useThreadPool() {
        // ExecutorService es = null;
        // es = Executors.newFixedThreadPool(4);
        // es = Executors.newCachedThreadPool();
        // es = Executors.newSingleThreadExecutor();
        // System.out.println(es);

        // Java线程池的完整构造函数
        // public ThreadPoolExecutor(
        //     int corePoolSize,                   // 线程池长期维持的线程数，即使线程处于Idle状态，也不会回收。
        //     int maximumPoolSize,                // 线程数的上限
        //     long keepAliveTime, TimeUnit unit,  // 超过corePoolSize的线程的idle时长，
        //                                         // 超过这个时间，多余的线程会被回收。
        //     BlockingQueue<Runnable> workQueue,  // 任务的排队队列
        // T   hreadFactory threadFactory,         // 新线程的产生方式
        //     RejectedExecutionHandler handler    // 拒绝策
        // )
        // 正确构造线程池, 避免使用无界队列, 明确拒绝任务时的行为
        int poolSize = Runtime.getRuntime().availableProcessors();
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
        RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
        ExecutorService rightES = new ThreadPoolExecutor(poolSize, poolSize,
                0, TimeUnit.SECONDS,
                queue,  // 使用有界队列, 避免 OOM
                policy);
        // 创建多个任务
        Collection<Callable<Result>> tasks = new ArrayList<>();
        tasks.add(new ShowDateTimeTask());
        tasks.add(new ShowDateTimeTask());
        tasks.add(new ShowDateTimeTask());
        tasks.add(new ShowDateTimeTask());
        // 提交多个任务
        CompletionService<Result> ecs = new ExecutorCompletionService<Result>(rightES);
        for (Callable<Result> task: tasks) {
            ecs.submit(task);
        }
        rightES.shutdown();
        // 获取任务返回值
        for (int i = 0; i < tasks.size(); i++) {
            try {
                Result r = ecs.take().get();    // 任意一个任务完成都会返回
                logger.info(r.getData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.debug(rightES.toString());
    }

    private class ShowDateTimeTask implements Callable<Result> {
        @Override
        public Result call() throws Exception {
            // 随机休眠,上限 1000 ms
            Random rand = new Random();
            Thread.sleep(rand.nextInt(5000));
            // 获取线程 ID
            long threadId = Thread.currentThread().getId();
            // 构造返回值
            Date now = new Date();
            String msg = String.format("[%d] %s", threadId, now.toString());
            return new Result(msg);
        }
    }

    private class Result {
        private String data;
        public Result(String data) { this.data = data; }
        public String getData() { return this.data; }
    }
}


