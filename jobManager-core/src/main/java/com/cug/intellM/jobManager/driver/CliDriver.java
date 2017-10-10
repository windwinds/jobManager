package com.cug.intellM.jobManager.driver;

import com.cug.intellM.jobManager.core.PluginLoader;
import com.cug.intellM.jobManager.plugin.JobManager;
import com.cug.intellM.jobManager.util.PluginUtils;

import java.net.MalformedURLException;

/**
 * Created by lyc on 2017/9/30.
 * 程序入口
 */
public class CliDriver {

    public static void main(String[] args) throws Exception {

        if (args.length < 1){
            System.out.println("缺少参数插件名称");
            System.exit(-1);
        }

        String pluginName = args[0];
        String pluginClassName = PluginLoader.getPluginClassName(pluginName);

        Class<?> cls = PluginUtils.loadClass(pluginName, pluginClassName);

        JobManager jobManager = (JobManager)cls.newInstance();

        jobManager.prepare();
        jobManager.check();
        jobManager.transformRule();
    }

}
