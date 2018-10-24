package com.test.java.util;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.Map;

/**
 * @author lvxw
 */
public class ParamUtils{
    /**
     * 读取classpath json格式的配置文件，并转成对象
     * @param path
     * @return
     */
    public static Map<String,Object> getClassPathFileContent(String path){
        ClassLoader classLoader = ParamUtils.class.getClassLoader();
        BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(path)));

        StringBuilder sb = new StringBuilder();
        String lineStr;
        try {
            while((lineStr = br.readLine()) != null) {
                sb.append(lineStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  jsonStrToMap(sb.toString());
    }

    /**
     * json字符串转map
     * @param jsonStr
     * @return
     */
    public static Map<String,Object> jsonStrToMap(String jsonStr){
        Map<String, Object> jsonMap = (Map<String, Object>) JSON.parse(jsonStr);
        return jsonMap;
    }
}

