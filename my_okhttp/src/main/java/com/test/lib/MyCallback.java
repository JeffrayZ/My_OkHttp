package com.test.lib;

import java.io.IOException;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.lib
 * @ClassName: MyCallback
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/1 16:55
 */
public interface MyCallback {
    void onFailure(MyCall call, IOException exception);
    void onResponse(MyCall call,MyResponse response);
}
