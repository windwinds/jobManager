package com.cug.intellM.jobManager.mysqlJobManager;

import com.cug.intellM.jobManager.plugin.JobManager;

/**
 * Created by lyc on 2017/9/30.
 */

//包外继承抽象类，抽象类的抽象方法必须声明为public，否则子类无法实现抽象方法
public class MysqlJobManager extends JobManager{

    public void prepare(){
        System.out.println("连接Mysql数据库");
    }

    public void check(){
        System.out.println("检查Mysql数据库表、字段，没有则创建");
    }

    public void transformRule(){
        System.out.println("将清洗条件转换为sql");
    }

}
