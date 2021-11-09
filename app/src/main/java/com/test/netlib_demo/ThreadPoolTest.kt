package com.test.netlib_demo

import java.util.concurrent.*

fun main(args: Array<String>) {
    val executorService: ExecutorService =
        ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS, SynchronousQueue<Runnable>(),
            ThreadFactory {
                Thread(it).apply {
                    name = "MyOkHttp Dispatcher"
                    isDaemon = false
                }
            })

    for (i in 1..10) {
        executorService.execute {
            try {
                Thread.sleep(1000)
                println("当前线程，执行耗时任务，线程是：" + Thread.currentThread().name)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}