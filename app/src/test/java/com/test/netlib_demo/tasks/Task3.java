package com.test.netlib_demo.tasks;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.netlib_demo
 * @ClassName: Task1
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/10/26 11:17
 */
public class Task3 implements IBaseTask{
    @Override
    public void doAction(String action, IBaseTask task) {
        if(action.equals("Task3")){
            System.out.println("Task3");
        } else {
            // 执行下一个任务节点
            task.doAction(action, task);
        }
    }
}
