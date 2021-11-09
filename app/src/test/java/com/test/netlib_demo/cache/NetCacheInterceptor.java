package com.test.netlib_demo.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.netlib_demo.cache
 * @ClassName: NetCacheResponse
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/10/27 14:51
 */
public class NetCacheInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response originResponse = chain.proceed(request);
        //设置响应的缓存时间为60秒，即设置Cache-Control头，并移除pragma消息头，因为pragma也是控制缓存的一个消息头属性
        originResponse = originResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "max-age=60")
                .build();
        return originResponse;
    }
}
