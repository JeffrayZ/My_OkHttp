package com.test.connection_pool;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 缓存的容器是一个双端队列,取的时候,根据HttpConnection的host和port两个字段查找,如果找到就返回对应的HttpConnection对象,否则返回null。
 * put的时候，要开启子线程去清空闲置时间已经大于最大闲置时间的连接，然后把连接对象存到双端队列中。
 */
public class ConnectionPool {

    // 队列，存放连接的对象
    private static ArrayDeque<HttpConnection> httpConnectionDeque;
    // 最大允许闲置时间
    private long keepAlive;
    // 用于判断清理连接池线程是否开启
    private boolean cleanRunnableFlag;

    public ConnectionPool() {
        // 默认一分钟
        this(1, TimeUnit.MINUTES);
    }

    public ConnectionPool(long keepAlive, TimeUnit timeUnit) {
        this.keepAlive = timeUnit.toMillis(keepAlive);
        httpConnectionDeque = new ArrayDeque<>();
    }

    // 开启一个线程，专门去检查连接池里面的连接对象
    private Runnable cleanRunnable = new Runnable() {

        @Override
        public void run() {
            while (true) {
                // 获取下一次检查的时间
                long nextCheckCleanTime = clean(System.currentTimeMillis());
                if(nextCheckCleanTime == -1){
                    return; // while (true) 结束了
                }
                if(nextCheckCleanTime > 0){
                    synchronized (ConnectionPool.this){
                        try {
                            ConnectionPool.this.wait(nextCheckCleanTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    private long clean(long currentTimeMillis) {
        long idleRecordSave = -1;
        synchronized (this) {
            Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
            while (iterator.hasNext()) {
                HttpConnection connection = iterator.next();
                // 当连接对象超过最大闲置时间，就会移除这个对象
                // 计算出来的闲置时间
                long idleTime = currentTimeMillis - connection.getHasUseTime();
                if (idleTime > keepAlive) {
                    // 移除
                    iterator.remove();
                    // 关闭Socket
                    connection.closeSocket();
                    // 清理 那些 连接对象
                    continue;
                }
                // 得到最长闲置时间
                if (idleRecordSave < idleTime) {
                    // idleRecordSave=10  >>>  idleRecordSave=20
                    idleRecordSave = idleTime;
                }
            }
            // 出来循环之后，idleRecordSave值计算完毕（闲置时间）
            // keepAlive=60s    idleRecordSave=30  60-30
            if (idleRecordSave >= 0) {
                return keepAlive - idleRecordSave;
            }
        }
        // 没有计算好，连接池里面没有连接对象，结束掉 线程池中的任务
        return idleRecordSave;
    }

    private Executor threadPoolExecutor =
            /**
             * 参数1：0            核心线程数 0
             * 参数2：MAX_VALUE    线程池中最大值
             * 参数3：60           单位值
             * 参数4：秒钟          时 分 秒
             * 参数5：队列          SynchronousQueue
             *
             * 执行任务大于（核心线程数） 启用（60s闲置时间）
             * 60秒闲置时间，没有过，复用之前的线程， 60秒过的，新实例化
             */
            new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "ConnectionPool");
                    thread.setDaemon(true); // 设置为守护线程
                    return thread;
                }
            });

    public synchronized void putConnection(HttpConnection connection) {
        // put的同时需要去检查连接池，判断是否需要清理
        if (!cleanRunnableFlag) {
            // 如果没有执行，就去执行
            cleanRunnableFlag = true;
            threadPoolExecutor.execute(cleanRunnable);
        }
        httpConnectionDeque.add(connection);
        int size = httpConnectionDeque.size();
        System.out.println("putConnection: size:" + size);
    }

    public HttpConnection getConnection(String host, int port){
        Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
        while(iterator.hasNext()){
            HttpConnection connection = iterator.next();
            if(connection.isConnectionAction(host,port)){
                iterator.remove();
                // 表示我们找到了
                return connection;
            }
        }
        return null;
    }
}
