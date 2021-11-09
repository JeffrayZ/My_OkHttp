package com.test.netlib_demo.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: NetLib_Demo
 * @Package: com.test.netlib_demo
 * @ClassName: TaskManager
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/10/26 11:14
 */
public class TaskManager implements IBaseTask{
    private List<IBaseTask> tasks = new ArrayList<>();

    public void addTask(IBaseTask task){
        tasks.add(task);
    }

    private int index = 0;

    @Override
    public void doAction(String action, IBaseTask task) {
        if(tasks.isEmpty()){
            System.out.println("tasks is empty");
            return;
        }

        if(index >= tasks.size()){
            System.out.println("index is " + index +" and tasks size is "+tasks.size());
            return;
        }

        IBaseTask currentTask = tasks.get(index);
        index++;
        currentTask.doAction(action,task);
    }
}
