package com.chen.library.thread;

import java.util.Observable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:DELL
 * date:2018/12/7
 */
public class ThreadFactoryManager extends Observable {
    private SynchronousQueue<Runnable> mSynchronousQueue = new SynchronousQueue<>();
    private ThreadPoolExecutor mExecutorService;
    private volatile static ThreadFactoryManager _instance = null;

    private ThreadFactoryManager() {
        mExecutorService = new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS, mSynchronousQueue);
        mExecutorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ThreadFactoryManager getInstance() {
        if (_instance == null) {
            synchronized (ThreadFactoryManager.class) {
                if (_instance == null) {
                    _instance = new ThreadFactoryManager();
                }
            }
        }
        return _instance;
    }

    public void executorTask(Runnable task) {
        mExecutorService.execute(task);
    }
}
