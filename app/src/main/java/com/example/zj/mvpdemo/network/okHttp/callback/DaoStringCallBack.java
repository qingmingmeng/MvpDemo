package com.example.zj.mvpdemo.network.okHttp.callback;

import com.example.zj.mvpdemo.network.NetResultCallBack;

import okhttp3.Call;


/**
 * 项目名称：HFAndroid
 * 项目作者：胡玉君
 * 创建日期：2017/6/3 14:51.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------------------------------------------------------
 */
public class DaoStringCallBack extends StringCallback
{
    public static final String SUCCESS = "00000";
    public static final String RETMSG = "retMsg";
    public static final String BODY = "json";
    public NetResultCallBack callBack;

    public DaoStringCallBack(NetResultCallBack callBack)
    {
        this.callBack = callBack;
    }

    @Override
    public void onError(Call call, Exception e, String flag){}

    @Override
    public void onResponse(String response, String flag) {}
}
