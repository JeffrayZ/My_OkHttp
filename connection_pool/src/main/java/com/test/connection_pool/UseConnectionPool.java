package com.test.connection_pool;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.connection_pool
 * @ClassName: UseConnectionPool
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/8 16:38
 */
public class UseConnectionPool {
    public void useConnectionPool(ConnectionPool pool,String host, int port){
        System.out.println("当前请求 >>> " + host + ":" +port);
        HttpConnection connection = pool.getConnection(host,port);
        if(connection == null){
            connection = new HttpConnection(host,port);
            System.out.println("连接池里面没有 连接对象，需要实例化一个连接对象...");
        } else {
            System.out.println("useConnectionPool: 复用和一个连接对象");
        }
        // 模拟请求
//        connection.getSocket().connect();
        // 把连接对象加入到连接池
        connection.setHasUseTime(System.currentTimeMillis()); // 更新使用时间
        pool.putConnection(connection);
        System.out.println("useConnectionPool: 给服务器发送请求 >>>>>>>> ");
    }
}
