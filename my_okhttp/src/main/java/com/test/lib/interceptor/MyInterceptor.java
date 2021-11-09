package com.test.lib.interceptor;

import com.test.lib.MyRequest;
import com.test.lib.MyResponse;

import java.io.IOException;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.interceptor
 * @ClassName: Interceptor
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/3 16:49
 */
public interface MyInterceptor {
    /**
     * 执行当前拦截器
     * @param chain 下一个任务
     * @return
     */
    MyResponse intercept(MyChain chain) throws IOException;

    interface MyChain{
        MyRequest request();

        /**
         * 在这个方法里面执行当前拦截器的 intercept 方法
         * @param request
         * @return
         */
        MyResponse proceed(MyRequest request) throws IOException;
    }
}
