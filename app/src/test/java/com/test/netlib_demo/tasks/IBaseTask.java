package com.test.netlib_demo.tasks;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.netlib_demo
 * @ClassName: IBaseTask
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/10/26 11:13
 */
public interface IBaseTask {
    void doAction(String action,IBaseTask task);
}
