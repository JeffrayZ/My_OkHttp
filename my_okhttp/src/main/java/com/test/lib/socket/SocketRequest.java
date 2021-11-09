package com.test.lib.socket;

import com.test.lib.MyRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib.socket
 * @ClassName: SocketRequest
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/2 11:32
 */
public class SocketRequest {
    private final String K = " ";
    private final String VERSION = "HTTP/1.1";
    private final String GRGN = "\r\n";

    // 通过request寻找到域名
    public String getHost(MyRequest request){
        try {
            URL url = new URL(request.url());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPort(MyRequest request) {
        try {
            URL url = new URL(request.url());
            int port = url.getPort();
            return port == -1 ? url.getDefaultPort() : port;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getRequestHeaderAll(MyRequest request){
        URL url = null;
        try {
            url = new URL(request.url());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // 拼接 请求头 的 请求行  GET /v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n
        String file = url.getFile();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(request.requestMethod())
                .append(K)
                .append(file)
                .append(K)
                .append(VERSION)
                .append(GRGN);
        /**
         * Content-Length: 48\r\n
         * Host: restapi.amap.com\r\n
         * Content-Type: application/x-www-form-urlencoded\r\n
         */
        if(!request.requestHeaders().isEmpty()){
            Map<String, String> headers = request.requestHeaders();
            for (Map.Entry<String, String> entry: headers.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append(":").append(K)
                        .append(entry.getValue())
                        .append(GRGN);
            }
        }
        // 拼接空行 必有
        stringBuffer.append(GRGN);
        // 如果是post请求，会有body
        if("POST".equalsIgnoreCase(request.requestMethod())){
            stringBuffer.append(request.requestBody().getBody()).append(GRGN);
        }

        return stringBuffer.toString();
    }
}
