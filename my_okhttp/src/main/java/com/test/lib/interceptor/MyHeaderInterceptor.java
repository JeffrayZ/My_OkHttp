package com.test.lib.interceptor;

import com.test.lib.MyRequest;
import com.test.lib.MyRequestBody;
import com.test.lib.MyResponse;
import com.test.lib.socket.SocketRequest;

import java.io.IOException;
import java.util.Map;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.interceptor
 * @ClassName: HeaderInterceptor
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/3 18:54
 */
public class MyHeaderInterceptor implements MyInterceptor {
    @Override
    public MyResponse intercept(MyChain chain) throws IOException {
        System.out.println("我是请求头拦截器，我要执行了~~~~");
        MyRequest request = chain.request();
        final Map<String, String> headers = request.requestHeaders();
        headers.put("Host",new SocketRequest().getHost(request));
        if("POST".equalsIgnoreCase(request.requestMethod())){
            // post 请求
            // 必须有的两个header Content-Type Content-Length
            headers.put("Content-Type", MyRequestBody.TYPE);
            headers.put("Content-Length",request.requestBody().getBody());
        }
        return chain.proceed(request);
    }
}
