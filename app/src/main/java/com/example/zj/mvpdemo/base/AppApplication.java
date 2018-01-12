package com.example.zj.mvpdemo.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：
 * 项目作者：胡玉君
 * 创建日期：2017/6/5 14:35.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------------------------------------------------------
 */
public class AppApplication extends Application {
    public static Context CONTEXT;
    public static final List<Activity> activities = new ArrayList<>();
    /**
     * 统一文件存储路径(如果有人新建文件夹请在此处添加)
     * <p>
     * 存储根目录：(此根目录为 sdcard/demo)
     * String path = Environment
     * .getExternalStoragePublicDirectory(SAVE_DATA_PATH)
     * .getPath();
     * <p>
     * 所有文件必须在根目录下根据不同的类型及功能自己建立子文件夹
     * String savePath = path + "/" + 子目录文件夹名称
     * <p>
     * 这才是最终文件的路径
     * String filePath = savePath + "/" + 文件名称
     * <p>
     */
    public static final String SAVE_DATA_PATH = "demo/";

    @Override
    public void onCreate(){
        CONTEXT = getApplicationContext();
        super.onCreate();
    }
}
