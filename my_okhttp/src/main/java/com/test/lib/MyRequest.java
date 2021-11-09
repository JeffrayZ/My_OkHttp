package com.test.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyRequest
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/1 16:53
 */
public class MyRequest {
    public static final String GET = "GET";
    public static final String POST = "POST";

    private String url;
    private String requestMethod = GET;
    private Map<String, String> headers = new HashMap<String, String>();
    private MyRequestBody requestBody;

    public String url() {
        return url;
    }

    public MyRequestBody requestBody() {
        return requestBody;
    }

    public Map<String, String> requestHeaders() {
        return headers;
    }

    public String requestMethod() {
        return requestMethod;
    }

    private MyRequest(Builder builder) {
        this.url = builder.url;
        this.requestMethod = builder.requestMethod;
        this.headers = builder.headers;
        this.requestBody = builder.requestBody;
    }

    public static final class Builder {
        public MyRequestBody requestBody;
        private String url;
        private String requestMethod = GET;
        private Map<String, String> headers = new HashMap<String, String>();

        public Builder url(String url) {
            this.url = url;
            return this;
        }


        public Builder get() {
            this.requestMethod = GET;
            return this;
        }

        public Builder addRequestHeader(String key,String value){
            headers.put(key,value);
            return this;
        }

        public Builder setRequestHeader(Map<String, String> headers){
            this.headers = headers;
            return this;
        }

        public Builder post(MyRequestBody requestBody){
            this.requestMethod = POST;
            this.requestBody = requestBody;
            return this;
        }

        public Builder setMethod(String method,MyRequestBody body){
            this.requestMethod = method;
            this.requestBody = body;
            return this;
        }

        public MyRequest build() {
            return new MyRequest(this);
        }
    }
}
