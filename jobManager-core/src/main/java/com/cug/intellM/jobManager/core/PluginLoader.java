package com.cug.intellM.jobManager.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cug.intellM.jobManager.util.Configuration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyc on 2017/9/30.
 */

//读取配置文件，加载插件配置信息
public class PluginLoader {

    public static final String PLUGIN_JSON = "core.json";
    //public static final String FILE_SEPARATOR = File.separator;
    private static Map<String, String> pluginMap;

    public static String getPluginClassName(String name){
        if (!pluginMap.containsKey(name)){
            throw new RuntimeException("该插件不存在，请仔细核对插件配置文件");
        }
        return pluginMap.get(name);
    }

    static {
        pluginMap = new HashMap<String, String>();
        String confPath = getConfPath();
        File coreFile = new File(confPath);
        Configuration conf = Configuration.from(coreFile);
        JSONArray ls = (JSONArray)conf.get("plugins");
        //将所有的插件信息存入pluginMap中
        for (Object o : ls){
            JSONObject json = (JSONObject)o;
            String pluginName = json.getString("pluginName");
            String pluginClass = json.getString("pluginClass");
            pluginMap.put(pluginName, pluginClass);
        }
        System.out.println(conf.get("plugins"));
    }

    //得到插件的配置文件完整的路径
    private static String getConfPath(){
        String dir = PluginLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            dir=URLDecoder.decode(dir,"utf-8");//转化成utf-8编码
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int num;
        //通过jar运行时路径的路径处理
        if ("jar".equals(dir.substring(dir.length()-3))){
            num = 2;
        }else{
            num = 3;
        }
        for (int i=0; i < num; i++){
            dir = dir.substring(0,dir.lastIndexOf('/'));
        }

        System.out.println(dir+"/"+"src"+"/"+"main"+"/"+"conf");
        String confDir = dir+"/"+"src/"+"main/"+"conf/" + "/" + PLUGIN_JSON;
        return confDir;
    }

}
