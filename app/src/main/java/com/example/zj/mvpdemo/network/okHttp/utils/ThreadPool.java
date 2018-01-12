package com.example.zj.mvpdemo.network.okHttp.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：AndroidStudy
 * 项目作者：胡玉君
 * 创建日期：2016/2/19 15:30.
 * --------------------------------------------------
 * 文件描述：线程池
 * --------------------------------------------------
 */
public class ThreadPool {
    private ThreadPoolProxy longPool;
    private ThreadPoolProxy shortPool;

    /** 单例+懒加载 */
    private ThreadPool() {}
    private static class SingleTonHolder{
        private static final ThreadPool instance = new ThreadPool();
    }
    public static ThreadPool getInstance(){
        return SingleTonHolder.instance;
    }

    /**
     * 创建网络线程池(网络线程可按双核手机)
     * @param corePoolSize 最大线程数，超出该数目则放入缓存队列(一般为手机cup核数 * 2 + 1)
     * @param cacheHandler 缓存队列最多缓存任务的数目，超出缓存数目则放入额外线程，建议与最大线程数一致
     * @param maximumPoolSize 额外线程数，超出额外线程则不执行，建议最多10个
     * @param aliveTime 存活时间,单位毫秒，建议5000
     */
    public synchronized ThreadPoolProxy creatLongPool(int corePoolSize, int cacheHandler, int maximumPoolSize, long aliveTime){
        if(longPool == null){
            longPool = new ThreadPoolProxy(corePoolSize, cacheHandler, maximumPoolSize, aliveTime);
        }
        return longPool;
    }
    public synchronized ThreadPoolProxy creatLongPool(){
        if(longPool == null){
            longPool = new ThreadPoolProxy(5, 5, 5, 5000);
        }
        return longPool;
    }

    /**
     * 创建本地线程池(网络线程可按单核手机)
     * @param corePoolSize 最大线程数，超出该数目则放入缓存队列(一般为手机cup核数 * 2 + 1)
     * @param cacheHandler 缓存队列最多缓存任务的数目，超出缓存数目则放入额外线程，建议与最大线程数一致
     * @param maximumPoolSize 额外线程数，超出额外线程则不执行，建议最多10个
     * @param aliveTime 存活时间,单位毫秒，建议5000
     */
    public synchronized ThreadPoolProxy creatShortPool(int corePoolSize, int cacheHandler, int maximumPoolSize, long aliveTime){
        if(shortPool == null){
            shortPool = new ThreadPoolProxy(corePoolSize, cacheHandler, maximumPoolSize, aliveTime);
        }
        return shortPool;
    }
    public synchronized ThreadPoolProxy creatShortPool(){
        if(shortPool == null){
            shortPool = new ThreadPoolProxy(3, 3, 3, 5000);
        }
        return shortPool;
    }

    public class ThreadPoolProxy{
        private ThreadPoolExecutor pool;
        private int corePoolSize;//核心线程数
        private int maximumPoolSize;//额外线程数
        private long aliveTime;//存活时间
        private int cacheHandler;//缓存任务数量

        /**
         * 线程池
         * @param corePoolSize 最大线程数，超出该数目则放入缓存队列
         * @param cacheHandler 缓存队列最多缓存任务的数目，超出缓存数目则放入额外线程
         * @param maximumPoolSize 额外线程数，超出额外线程则不执行
         * @param aliveTime 存活时间
         */
        public ThreadPoolProxy(int corePoolSize, int cacheHandler, int maximumPoolSize, long aliveTime){
            this.corePoolSize = corePoolSize;
            this.cacheHandler = cacheHandler;
            this.maximumPoolSize = maximumPoolSize;
            this.aliveTime = aliveTime;
        }
        public void execute(Runnable runnable){
            if(pool == null){
                pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, aliveTime,
                        TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(cacheHandler));
            }
            pool.execute(runnable);
        }
        public void cancel(Runnable runnable){
            if(pool != null && !pool.isShutdown() && pool.isTerminated()){
                pool.remove(runnable);
            }
        }
    }
}
