package com.test.lib;

import com.test.lib.socket.SocketRequest;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyDispatcher
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/2 11:03
 */
public class MyDispatcher {

    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private ExecutorService executorService;

    // 运行队列
    private Deque<MyRealCall.MyAsyncCall> runningAsyncCalls = new ArrayDeque<>();
    // 等待队列
    private Deque<MyRealCall.MyAsyncCall> readyAsyncCalls = new ArrayDeque<>();

    // 同步执行  单独的一个队列
    private Deque<MyRealCall> runningSyncCalls = new ArrayDeque<>();

    // 同时访问数
    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    // 同时访问同一个服务器域名数
    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    public void enqueue(MyRealCall.MyAsyncCall call) {
        // 同时运行的队列数必须小于配置的数（64个）
        // 同时访问同一个服务器域名不能超过5个
        if (runningAsyncCalls.size() < maxRequests
                && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    public void execute(MyRealCall call) {
        runningSyncCalls.add(call);
    }

    private synchronized Executor executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("自定义线程.........");
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return executorService;
    }

    // 同时访问同一个域名的数量
    private int runningCallsForHost(MyRealCall.MyAsyncCall call) {
        int result = 0;
        SocketRequest sr = new SocketRequest();
        for (MyRealCall.MyAsyncCall c :
                runningAsyncCalls) {
            if (sr.getHost(c.getRequest()).equals(sr.getHost(call.getRequest()))) {
                result++;
            }
        }
        return result;
    }

    // 异步
    public void finish(MyRealCall.MyAsyncCall call) {
        // 移除当前任务
        runningAsyncCalls.remove(call);

        // 等待队列处理
        for (MyRealCall.MyAsyncCall readyAsyncCall : readyAsyncCalls) {
            // 从等待队列移除
            readyAsyncCalls.remove(readyAsyncCall);
            // 添加到运行队列
            runningAsyncCalls.add(readyAsyncCall);
            // 执行任务
            executorService().execute(readyAsyncCall);
        }
    }

    // 同步
    public void finished(MyRealCall call) {
        runningSyncCalls.remove(call);
    }
}
