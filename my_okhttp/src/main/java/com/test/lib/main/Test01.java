package com.test.lib.main;

import com.test.lib.MyCall;
import com.test.lib.MyCallback;
import com.test.lib.MyOkhttpClient;
import com.test.lib.MyRequest;
import com.test.lib.MyResponse;

import java.io.IOException;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.main
 * @ClassName: Test01
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/5 16:25
 */
public class Test01 {
    public static void main(String[] args) throws Exception {
        MyOkhttpClient client = new MyOkhttpClient.Builder().build();

        MyRequest request = new MyRequest.Builder().url("http://publicobject.com/helloworld.txt").build();

        MyCall call = client.newCall(request);

        // 异步请求
        call.enqueue(new MyCallback(){

            @Override
            public void onFailure(MyCall call, IOException exception) {
                System.out.println(exception.getMessage());
            }

            @Override
            public void onResponse(MyCall call, MyResponse response) {
                System.out.println(response.string());
            }
        });

//        同步请求
//        val response = call.execute()
//        println("$response")
    }
}
