package com.test.lib.interceptor;

import com.test.lib.MyRequest;
import com.test.lib.MyResponse;
import com.test.lib.socket.SocketRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.interceptor
 * @ClassName: MyCallServerInterceptor
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/3 18:58
 */
public class MyCallServerInterceptor implements MyInterceptor {
    @Override
    public MyResponse intercept(MyChain chain) throws IOException {
        System.out.println("我是Socket连接请求拦截器，我要执行了~~~~");
        SocketRequest sr = new SocketRequest();
        MyRequest request = chain.request();

        // 和服务器创建连接
        Socket socket;
        if(request.url().startsWith("https")){
            socket = (SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(sr.getHost(request), sr.getPort(request));
        } else {
            socket = new Socket(sr.getHost(request), sr.getPort(request));
        }
        // 本地发送给服务器的信息
        OutputStream os = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        String requestHeaderAll = sr.getRequestHeaderAll(request);
        System.out.println("requestHeaderAll:" + requestHeaderAll);
        bufferedWriter.write(requestHeaderAll);
        bufferedWriter.flush();


        BufferedWriter fileBufferWriter = new BufferedWriter(new FileWriter("D:\\response.log", true));

        // 获取服务器的响应
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        MyResponse response = new MyResponse();
        String lineString;
        // 第一行 HTTP/1.1 200 OK
        lineString = bufferedReader.readLine();
        fileBufferWriter.write(lineString + "\r\n");
        String[] s1 = lineString.split(" ");
        response.setStatusCode(Integer.parseInt(s1[1]));

        // 第二行开始是响应头 key:value
        while ((lineString = bufferedReader.readLine()) != null) {
            if ("".equals(lineString)) {
                // 读到空行，说明下面开始就是响应体 body
                fileBufferWriter.write(lineString + "\r\n");
                break;
            } else {
                fileBufferWriter.write(lineString + "\r\n");
                String[] s2 = lineString.split(": ");
                response.addHeaders(s2[0], s2[1]);
            }
        }

        // 最后处理响应体 body
        StringBuffer bodyBuffer = new StringBuffer();
        while ((lineString = bufferedReader.readLine()) != null) {
            fileBufferWriter.write(lineString + "\r\n");
            bodyBuffer.append(lineString);
        }
        response.body = bodyBuffer.toString();


        fileBufferWriter.write("=======================================================================" + "\r\n");
        fileBufferWriter.flush();
        fileBufferWriter.close();

        return response;
    }
}
