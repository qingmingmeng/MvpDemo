package com.example.zj.mvpdemo.base;

import com.example.zj.mvpdemo.network.NetResultCallBack;

import java.lang.ref.WeakReference;


/**
 * 项目名称：
 * 项目作者：胡玉君
 * 创建日期：2017/6/4 20:13.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------------------------------------------------------
 */
public abstract class BaseModel {

    private final WeakReference<NetResultCallBack> reference;
    public BaseModel(NetResultCallBack callBack) {
        reference = new WeakReference(callBack);
    }
    
    protected NetResultCallBack callBack(){
        if(reference != null){
            return reference.get();
        }
        return null;
    }

    public abstract Object parseResponse(String response);
}
