package com.example.zj.mvpdemo.network.okHttp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：Gosn帮助类
 * 项目作者：胡玉君
 * 创建日期：2016/5/3 13:45.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------------------------------------------------------
 */
public class GsonHelper {

    private Gson gson;
    private GsonHelper(){
        gson = new Gson();
    }
    private static class SingleTonHolder{
        private static final GsonHelper instance = new GsonHelper();
    }
    public static GsonHelper getInstance(){
        return SingleTonHolder.instance;
    }

    /** json转类 */
    public <T> T json2Class(String json, Class<T> cls){
        return gson.fromJson(json, cls);
    }
    /** 类转json */
    public <T> String class2Json(T cls){
        return gson.toJson(cls);
    }
    /** map转json */
    public String map2Json(Map<? extends Object, ? extends Object> map){
        return gson.toJson(map);
    }

    /** json转List集合 */
    public <T> List<T> json2list(String json, Class<T> clazz) {
        List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            lst.add(gson.fromJson(elem, clazz));
        }
        return lst;
    }

    /** list转json */
    public <T> String list2Json(List<T> list){
        return gson.toJson(list);
    }

    /** 转换复杂的Object */
    public Object getComplexBeanFromJson(String jsonStr, String className,HashMap<String,String> childNameMap)
    {
        try {

            if (jsonStr.equals("")) {
                return null;
            }
            JSONObject jsonObjectNext = new JSONObject(jsonStr);
            Object objCla;
            try {
                objCla = Class.forName(className).newInstance();
                Object objInstance = Class.forName(className).newInstance();
                Field[] fields = objCla.getClass().getDeclaredFields();
                int fieldLength = fields.length;
                for (int i = 0; i < fieldLength; i++) {
                    Log.i("", String.valueOf(i));
                    Log.i("fields_------", fields[i].toString());
                    String tagName=fields[i].getName();
                    Object subObject = jsonObjectNext.opt(fields[i].getName());
                    if (subObject == null) {
                        continue;
                    }

                    String tagValue = subObject.toString();
                    Log.i("tagValue", tagValue);


                    Class typeName = fields[i].getType();
                    if (long.class.equals(typeName)) {
                        long tagValuel = Long.parseLong(tagValue);
                        fields[i].set(objInstance, tagValuel);

                    } else if (int.class.equals(typeName)) {
                        int tagValueint = Integer.parseInt(tagValue);
                        fields[i].set(objInstance, tagValueint);
                    } else if (float.class.equals(typeName)) {
                        float tagValueFloat = Float.parseFloat(tagValue);

                        fields[i].set(objInstance, tagValueFloat);

                    } else if (String.class.equals(typeName)) {
                        fields[i].set(objInstance, tagValue);
                    }else if(List.class.equals(typeName))
                    {
                        if(childNameMap==null||childNameMap.size()==0)
                        {
                            fields[i].set(objInstance,
                                    gson.fromJson(tagValue, fields[i].getType()));
                            continue;
                        }
                        String listclassName="";

                        boolean hasclassTag= childNameMap.containsKey(tagName);
                        if(hasclassTag)
                        {
                            listclassName=childNameMap.get(tagName);

                        }else {
                            listclassName = childNameMap.get("key");
                        }


                        List<?> resultList =getListFormJsonArrayWithOutName(tagValue, listclassName);

                        fields[i].set(objInstance, resultList);

                    }else if(Array.class.equals(typeName))
                    {
                        if(childNameMap==null||childNameMap.get(0)==null)
                        {
                            fields[i].set(objInstance,
                                    gson.fromJson(tagValue, fields[i].getType()));
                            continue;
                        }
                        String listclassName="";

                        boolean hasclassTag= childNameMap.containsKey(tagName);
                        if(hasclassTag)
                        {
                            listclassName=childNameMap.get(tagName);

                        }else {
                            listclassName = childNameMap.get("key");
                        }
                        List<?> resultList =getListFormJsonArrayWithOutName(tagValue, listclassName);

                        fields[i].set(objInstance, resultList);
                    }
                    else {
                        fields[i].set(objInstance,
                                gson.fromJson(tagValue, fields[i].getType()));
                    }

                }
                return objInstance;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }



//    #################################################以下为okHttp解析用####################################

    /**
     * @param jsonString 要转换的字符串
     * @param arrayName  json数组属性的名字
     * @param className  数组中对象的className,带包名

     * @return
     */
    public List<?> getListFormJsonArray(String jsonString, String arrayName, String className) {
        List<Object> list = new ArrayList<Object>();
        try {

            String jsonStr = null;


            JSONObject jsonObject2 = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject2.getJSONArray(arrayName);

            list = new ArrayList<Object>();
            Object objCla = Class.forName(className).newInstance();
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                Log.i("i", String.valueOf(i));

                jsonStr = jsonArray.get(i).toString();

                jsonArray.put(i, null);

                objCla = gson.fromJson(jsonStr, objCla.getClass());
                list.add(objCla);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     *
     *
     * @param jsonString

     * @param className

     * @return
     */

    public List<?> getListFormJsonArrayWithOutName(String jsonString, String className) {
        List<Object> list = new ArrayList<>();
        try {

            String jsonStr;
            JSONArray jsonArray =new  JSONArray(jsonString);
            list = new ArrayList<>();
            Object objCla = Class.forName(className).newInstance();
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                Log.i("i", String.valueOf(i));
                jsonStr = jsonArray.get(i).toString();
                jsonArray.put(i, null);
                objCla = gson.fromJson(jsonStr, objCla.getClass());
                list.add(objCla);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }
}
