package com.mrcd.xrouter.gradle.plugin.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取缓存目录下的JSon文件工具类
 */
public class JsonIO {

    public static List<ClassPath> readJsonFile(File file) {
        List<ClassPath> paths = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String temp;
            while ((temp = reader.readLine()) != null) {
                buffer.append(temp);
            }
            String json = buffer.toString();
            if (StringUtils.isEmpty(json)) {
                return paths;
            }
            Gson gson = new Gson();
            TypeToken<List<ClassPath>> list = new TypeToken<List<ClassPath>>() {
            };
            paths = gson.fromJson(json, list.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }

}
