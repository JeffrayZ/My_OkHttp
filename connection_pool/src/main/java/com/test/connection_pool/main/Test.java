package com.test.connection_pool.main;

import com.test.connection_pool.ConnectionPool;
import com.test.connection_pool.UseConnectionPool;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.connection_pool.main
 * @ClassName: Test
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/8 16:44
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ConnectionPool pool = new ConnectionPool();

        UseConnectionPool useConnectionPool = new UseConnectionPool();
        new Thread() {

            @Override
            public void run() {
                super.run();

                useConnectionPool.useConnectionPool(pool, "restapi.amap.com", 80);
                useConnectionPool.useConnectionPool(pool, "restapi.amap.com", 80);
                useConnectionPool.useConnectionPool(pool, "restapi.amap.com", 80);
                useConnectionPool.useConnectionPool(pool, "restapi.amap.com", 80);
                useConnectionPool.useConnectionPool(pool, "restapi.amap.com", 80);
            }
        }.start();

        useConnectionPool.useConnectionPool(pool, "www.baidu.com", 80);
        useConnectionPool.useConnectionPool(pool, "www.baidu.com", 80);
        useConnectionPool.useConnectionPool(pool, "www.baidu.com", 80);
        useConnectionPool.useConnectionPool(pool, "www.sina.com", 80);
        useConnectionPool.useConnectionPool(pool, "www.sina.com", 80);
        useConnectionPool.useConnectionPool(pool, "www.sina.com", 80);
    }
}
