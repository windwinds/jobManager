package com.cug.intellM.jobManager.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import com.alibaba.fastjson.JSON;

/**
 * Created by lyc on 2017/10/9.
 *Configuration 提供多级JSON配置信息无损存储
 */
public class Configuration {

    private static final Pattern VARIABLE_PATTERN = Pattern
            .compile("(\\$)\\{?(\\w+)\\}?");
    private Object root = null;

    /**
     * 从JSON字符串加载Configuration
     */
    public static Configuration from(String json) {
        json = replaceVariable(json);
        checkJSON(json);

        try {
            return new Configuration(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 从包括json的File对象加载Configuration
     */
    public static Configuration from(File file) {
        try {
            return Configuration.from(IOUtils.toString(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(
                    String.format("配置信息错误，您提供的配置文件[%s]不存在. 请检查您的配置文件.", file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("配置信息错误. 您提供配置文件[%s]读取失败，错误原因: %s. 请检查您的配置文件的权限设置.",
                            file.getAbsolutePath(), e));
        }
    }

    private void checkPath(final String path) {
        if (null == path) {
            throw new IllegalArgumentException(
                    "系统编程错误, 该异常代表系统编程错误, 请联系开发团队!.");
        }

        for (final String each : StringUtils.split(".")) {
            if (StringUtils.isBlank(each)) {
                throw new IllegalArgumentException(String.format(
                        "系统编程错误, 路径[%s]不合法, 路径层次之间不能出现空白字符 .", path));
            }
        }
    }

    private static void checkJSON(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new RuntimeException(
                    "配置信息错误. 因为您提供的配置信息不是合法的JSON格式, JSON不能为空白. 请按照标准json格式提供配置信息. ");
        }
    }

    public static String replaceVariable(final String param) {
        Map<String, String> mapping = new HashMap<String, String>();

        Matcher matcher = VARIABLE_PATTERN.matcher(param);
        while (matcher.find()) {
            String variable = matcher.group(2);
            String value = System.getProperty(variable);
            if (StringUtils.isBlank(value)) {
                value = matcher.group();
            }
            mapping.put(matcher.group(), value);
        }

        String retString = param;
        for (final String key : mapping.keySet()) {
            retString = retString.replace(key, mapping.get(key));
        }

        return retString;
    }

    private Configuration(final String json) {
        try {
            this.root = JSON.parse(json);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("配置信息错误. 您提供的配置信息不是合法的JSON格式: %s . 请按照标准json格式提供配置信息. ", e.getMessage()));
        }
    }

    /**
     * 根据用户提供的json path，寻址具体的对象。
     * <p/>
     * <br>
     * <p/>
     * NOTE: 目前仅支持Map以及List下标寻址, 例如:
     * <p/>
     * <br />
     * <p/>
     * 对于如下JSON
     * <p/>
     * {"a": {"b": {"c": [0,1,2,3]}}}
     * <p/>
     * config.get("") 返回整个Map <br>
     * config.get("a") 返回a下属整个Map <br>
     * config.get("a.b.c") 返回c对应的数组List <br>
     * config.get("a.b.c[0]") 返回数字0
     *
     * @return Java表示的JSON对象，如果path不存在或者对象不存在，均返回null。
     */
    public Object get(final String path) {
        this.checkPath(path);
        try {
            return this.findObject(path);
        } catch (Exception e) {
            return null;
        }
    }

    private Object findObject(final String path) {
        boolean isRootQuery = StringUtils.isBlank(path);
        if (isRootQuery) {
            return this.root;
        }

        Object target = this.root;

        for (final String each : split2List(path)) {
            if (isPathMap(each)) {
                target = findObjectInMap(target, each);
                continue;
            } else {
                target = findObjectInList(target, each);
                continue;
            }
        }

        return target;
    }

    private Object findObjectInMap(final Object target, final String index) {
        boolean isMap = (target instanceof Map);
        if (!isMap) {
            throw new IllegalArgumentException(String.format(
                    "您提供的配置文件有误. 路径[%s]需要配置Json格式的Map对象，但该节点发现实际类型是[%s]. 请检查您的配置并作出修改.",
                    index, target.getClass().toString()));
        }

        Object result = ((Map<String, Object>) target).get(index);
        if (null == result) {
            throw new IllegalArgumentException(String.format(
                    "您提供的配置文件有误. 路径[%s]值为null，datax无法识别该配置. 请检查您的配置并作出修改.", index));
        }

        return result;
    }

    private Object findObjectInList(final Object target, final String each) {
        boolean isList = (target instanceof List);
        if (!isList) {
            throw new IllegalArgumentException(String.format(
                    "您提供的配置文件有误. 路径[%s]需要配置Json格式的Map对象，但该节点发现实际类型是[%s]. 请检查您的配置并作出修改.",
                    each, target.getClass().toString()));
        }

        String index = each.replace("[", "").replace("]", "");
        if (!StringUtils.isNumeric(index)) {
            throw new IllegalArgumentException(
                    String.format(
                            "系统编程错误，列表下标必须为数字类型，但该节点发现实际类型是[%s] ，该异常代表系统编程错误, 请联系DataX开发团队 !",
                            index));
        }

        return ((List<Object>) target).get(Integer.valueOf(index));
    }

    private List<String> split2List(final String path) {
        return Arrays.asList(StringUtils.split(split(path), "."));
    }

    private String split(final String path) {
        return StringUtils.replace(path, "[", ".[");
    }

    private boolean isPathMap(final String path) {
        return StringUtils.isNotBlank(path) && !isPathList(path);
    }

    private boolean isPathList(final String path) {
        return path.contains("[") && path.contains("]");
    }

}
