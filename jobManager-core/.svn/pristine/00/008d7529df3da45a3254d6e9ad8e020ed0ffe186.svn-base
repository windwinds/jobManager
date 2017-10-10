package com.cug.intellM.jobManager.util;

import com.cug.intellM.jobManager.core.PluginLoader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by lyc on 2017/9/30.
 * 根据插件信息，通过反射得到插件类的对象
 */
public class PluginUtils {

    //线程安全的hashMap
    private static Map<String, ClassLoader> cache = new ConcurrentHashMap();

    //通过插件名称得到该插件的jar的URL
    private static URL listFileByPluginName(String pluginName) throws MalformedURLException {

        String dir = PluginLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            dir= URLDecoder.decode(dir,"utf-8");//转化成utf-8编码
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
        String confDir = dir+"/"+"src/"+"main/"+"pluginLib/" + "/" + pluginName + ".jar";
        File jar = new File(confDir);
        if (!jar.exists()) {
            throw new RuntimeException("Plugin not found: " + pluginName);
        }
        URL url = jar.toURI().toURL();
        return url;
    }

    //通过插件名称和类名反射得到java类加载器
    public static Class<?> loadClass(String pluginName, String className) throws ClassNotFoundException, MalformedURLException {
        URL url = listFileByPluginName(pluginName);
        ClassLoader classLoader = cache.get(pluginName);
        if (classLoader == null) {
            classLoader = new URLClassLoader(new URL[]{url});
            cache.put(pluginName, classLoader);
        }
        return classLoader.loadClass(className);
    }

    public static Class<?> loadClass2(String pluginName, String className) throws Exception {
        URL url = listFileByPluginName(pluginName);

        //得到系统类加载器
        URLClassLoader urlClassLoader= (URLClassLoader) ClassLoader.getSystemClassLoader();
        //因为URLClassLoader中的addURL方法的权限为protected所以只能采用反射的方法调用addURL方法
        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
        add.setAccessible(true);
        add.invoke(urlClassLoader, new Object[] {url});
        Class c =Class.forName(className);
        return c;
    }

    public static void main(String [] args)throws ClassNotFoundException, MalformedURLException{
        listFileByPluginName("mysqlJobManager");

    }


}
