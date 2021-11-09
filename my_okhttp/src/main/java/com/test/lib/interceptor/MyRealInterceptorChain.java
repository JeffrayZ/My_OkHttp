package com.test.lib.interceptor;

import com.test.lib.MyCall;
import com.test.lib.MyRequest;
import com.test.lib.MyResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.interceptor
 * @ClassName: MyRealInterceptorChain
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/3 17:04
 */
public class MyRealInterceptorChain implements MyInterceptor.MyChain {
    private final List<MyInterceptor> interceptors;
    private final MyRequest request;
    private int index;
    private final MyCall call;

    public MyRealInterceptorChain(List<MyInterceptor> interceptors, MyRequest request, int index, MyCall call) {
        this.interceptors = interceptors;
        this.request = request;
        this.index = index;
        this.call = call;
    }

    @Override
    public MyRequest request() {
        return request;
    }

    @Override
    public MyResponse proceed(MyRequest request) throws IOException {
        // 不能大于这个
        if(index >= interceptors.size()){
            throw new AssertionError();
        }
        // 预先初始化下一个chain，也就是下一个使用下一个拦截器需要的参数
        final MyRealInterceptorChain chain = new MyRealInterceptorChain(interceptors,request,index + 1,call);
        // 取出当前index的拦截器，默认是0，也就是第一个
        MyInterceptor interceptor = interceptors.get(index);
        // 执行当前拦截器，并将下一个拦截器需要的chain带过去
        // 那么第一个拦截器的chain是怎么来的呢？答案是 RealCall 里面的 getResponseWithInterceptorChain 方法
        MyResponse response = interceptor.intercept(chain);
        return response;
    }
}
