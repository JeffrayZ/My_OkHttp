package com.test.lib;

import java.io.IOException;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyCall
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/1 16:53
 */
public interface MyCall {
    // 异步执行
    void enqueue(MyCallback callback);
    // 同步执行
    MyResponse execute() throws IOException;
}
