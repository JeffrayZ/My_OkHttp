package com.test.lib;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyOkhttpClient
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/1 16:49
 */
public class MyOkhttpClient {
    private MyDispatcher dispatcher;
    private int retryCount;
    // 表示用户取消了请求
    private boolean isCanceled = false;

    private MyOkhttpClient(Builder builder){
        this.dispatcher = builder.dispatcher;
        this.retryCount = builder.retryCount;
        this.isCanceled = builder.isCanceled;
    }

    public boolean canceled(){
        return isCanceled;
    }


    // 重试次数
    public int retryCount(){
        return retryCount;
    }

    public MyCall newCall(MyRequest request){
        return new MyRealCall(this,request);
    }

    public MyDispatcher dispatcher(){
        return this.dispatcher;
    }

    public static final class Builder {
        public boolean isCanceled;
        private MyDispatcher dispatcher;
        // 重试次数
        private int retryCount = 3;

        public Builder(){
            this.dispatcher = new MyDispatcher();
        }

        public Builder dispatcher(MyDispatcher dispatcher){
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder retryCount(int count){
            this.retryCount = retryCount;
            return this;
        }

        public Builder canceled(){
            isCanceled = true;
            return this;
        }

        public MyOkhttpClient build(){
            return new MyOkhttpClient(this);
        }
    }
}
