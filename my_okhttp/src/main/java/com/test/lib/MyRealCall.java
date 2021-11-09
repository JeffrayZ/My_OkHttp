package com.test.lib;

import com.test.lib.interceptor.MyCallServerInterceptor;
import com.test.lib.interceptor.MyHeaderInterceptor;
import com.test.lib.interceptor.MyInterceptor;
import com.test.lib.interceptor.MyRealInterceptorChain;
import com.test.lib.interceptor.MyRetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyRealCall
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/2 10:53
 */
public class MyRealCall implements MyCall {
    private boolean executed = false;
    private MyOkhttpClient client;
    private MyRequest request;

    @Override
    public void enqueue(MyCallback callback) {
        synchronized (this) {
            // 不能被重复执行
            if (executed) {
                throw new IllegalStateException("Already Executed");
            }
            executed = true;
        }
        client.dispatcher().enqueue(new MyAsyncCall(callback));
    }

    @Override
    public MyResponse execute() throws IOException{
        try {
            client.dispatcher().execute(this);
            return getResponseWithInterceptorChain();
        } finally {
            client.dispatcher().finished(this);
        }
    }

    public MyRealCall(MyOkhttpClient client, MyRequest request) {
        this.client = client;
        this.request = request;
    }

    public final class MyAsyncCall implements Runnable {
        private MyCallback callback;

        public MyAsyncCall(MyCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean signalledCallback = false;
            try {
                MyResponse response = getResponseWithInterceptorChain();
                if (client.canceled()) {
                    signalledCallback = true;
                    callback.onFailure(MyRealCall.this, new IOException("用户取消..."));
                } else {
                    signalledCallback = true;
                    callback.onResponse(MyRealCall.this, response);
                }
            } catch (Exception e) {
                if (signalledCallback) {
                    System.out.println("用户再使用过程中 出错了...");
                } else {
                    callback.onFailure(MyRealCall.this, new IOException("OKHTTP getResponseWithInterceptorChain 错误... e:" + e.toString()));
                }
            } finally {
                client.dispatcher().finish(this);
            }
        }



        public MyRequest getRequest() {
            return MyRealCall.this.request;
        }
    }

    private MyResponse getResponseWithInterceptorChain() throws IOException {
        final List<MyInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new MyRetryInterceptor()); // 重试拦截器
        interceptors.add(new MyHeaderInterceptor()); // 请求头拦截器
        interceptors.add(new MyCallServerInterceptor()); // // 连接服务器的拦截器
        MyRealInterceptorChain chain = new MyRealInterceptorChain(interceptors, request, 0, MyRealCall.this);
        MyResponse response = chain.proceed(request);
        return response;
    }
}
