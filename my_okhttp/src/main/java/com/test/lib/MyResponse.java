package com.test.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyResponse
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/1 16:58
 */
public class MyResponse {
    private int statusCode;
    public String body;
    private Map<String, String> headers = new HashMap<String, String>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeaders(String key,String value) {
        headers.put(key,value);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String string() {
        return body;
    }

    @Override
    public String toString() {
        return "MyResponse{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                '}';
    }
}
