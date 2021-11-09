package com.test.lib.interceptor;

import com.test.lib.MyRequest;
import com.test.lib.MyResponse;

import java.io.IOException;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.interceptor
 * @ClassName: RetryInterceptor
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/3 17:01
 */
public class MyRetryInterceptor implements MyInterceptor {
    private static final int retryCount = 3;

    @Override
    public MyResponse intercept(MyChain chain) throws IOException {
        System.out.println("我是重试拦截器，我要执行了~~~~");
        MyRequest request = chain.request();
        for (int i = 0; i < retryCount; i++) {
            try {
                MyResponse response = chain.proceed(request);
                switch (response.getStatusCode()) {
                    case 301:
                        MyRequest localRequest = new MyRequest.Builder()
                                .setMethod(request.requestMethod(), request.requestBody())
                                .url(response.getHeaders().get("Location"))
                                .setRequestHeader(request.requestHeaders())
                                .build();
                        response = chain.proceed(localRequest);
                        break;
                    default:
                        break;
                }
                return response;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        throw new IOException("重试次数超过三次，抛出异常");
    }
}
