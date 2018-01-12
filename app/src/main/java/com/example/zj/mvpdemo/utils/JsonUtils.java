package com.example.zj.mvpdemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */

public class JsonUtils {
    //类转json
    public static <T> String class2Json(T cls) {
        return new Gson().toJson(cls);
    }

    /**
     * json转类
     * 解决方法：com.google.gson.JsonSyntaxException:com.google.gson.stream.MalformedJsonException错误
       不直接解析Json数据对应的字符串，将拿到的Json数据字符串经过JsonReader处理后再将JsonReader对象传入Gson方法进行解析。示例代码如下：
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T json2Class(String json, Class<T> cls) {
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new StringReader(json));//其中jsonContext为String类型的Json数据
        jsonReader.setLenient(true);
        return gson.fromJson(jsonReader, cls);
    }

    //json转list
    public static <T> List<T> json2list(String json, Class<T> clazz) {
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        Gson gson = new Gson();
        for (final JsonElement elem : array) {
            lst.add(gson.fromJson(elem, clazz));
        }
        return lst;
    }

    //list转json
    public static <T> String list2Json(List<T> list) {
        return new Gson().toJson(list);
    }
}
