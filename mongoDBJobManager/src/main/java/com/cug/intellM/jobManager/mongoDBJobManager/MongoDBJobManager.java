package com.cug.intellM.jobManager.mongoDBJobManager;

import com.cug.intellM.jobManager.plugin.JobManager;

/**
 * Created by lyc on 2017/10/10.
 */
public class MongoDBJobManager extends JobManager {

    public void prepare() {
        System.out.println("连接MongoDB数据库");
    }

    public void check() {
        System.out.println("检查MongoDB数据库表、字段，没有则创建");
    }

    public void transformRule() {
        System.out.println("将清洗条件转换为MongoDB");
    }
}
