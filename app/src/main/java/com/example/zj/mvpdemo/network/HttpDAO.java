package com.example.zj.mvpdemo.network;

import com.example.zj.mvpdemo.network.okHttp.OkHttpUtils;
import com.example.zj.mvpdemo.network.okHttp.builder.GetBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PostStringBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PutStringBuilder;
import com.example.zj.mvpdemo.network.okHttp.callback.DaoStringCallBack;
import com.example.zj.mvpdemo.utils.JsonUtils;

import java.util.Map;

/**
 * 项目名称：HFAndroid
 * 项目作者：胡玉君
 * 创建日期：2017/6/3 14:44.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * ----------------------------------------------------------------------------------------------------
 */
public class HttpDAO
{
    private DaoStringCallBack callback;
    private Object tag;

    private String appVersion;
    private String token;

    public HttpDAO(NetResultCallBack netResultCallBack)
    {
        this.tag = netResultCallBack;
        //token = SpHelper.getInstance().readMsgFromSp(SpKey.LOGIN, SpKey.LOGIN_ACCESSTOKEN);
        callback = new DaoStringCallBack(netResultCallBack);
    }

    /**
     * 可调用方法，requestBean是请求参数的封装类
     */
    public <E> void put(String url, E requestParam, String flag)
    {
        PutStringBuilder builder = OkHttpUtils.putString()
                .tag(tag)
                .url(BaseUrl.baseUrl + url)
                .content(JsonUtils.class2Json(requestParam))
                .addHeader("Connection", "close")
                .addHeader("APPVersion", "AND-P-" + appVersion)
                .addHeader("DeviceModel", "AND-P-" + android.os.Build.MODEL)
                .addHeader("DeviceResolution", "")
                .addHeader("SysVersion", "AND-P-" + android.os.Build.VERSION.RELEASE)
                .addHeader("channel", "")
                .addHeader("channel_no", "");

//        if (!NO_TOKEN.contains(url))
//        {
            builder.addHeader("Authorization", "Bearer" + token)
                    .addHeader("access_token", token);
//        }
        builder.build().execute(callback, flag);
    }


    public void get(String url, Map<String, String> requestParam, String flag)
    {
        GetBuilder builder = OkHttpUtils
                .get()
                .tag(tag)
                .url(BaseUrl.baseUrl + url)
                .params(requestParam)
                .addHeader("Connection", "close")
                .addHeader("APPVersion", "AND-P-" + appVersion)
                .addHeader("DeviceModel", "AND-P-" + android.os.Build.MODEL)
                .addHeader("DeviceResolution", "")
                .addHeader("SysVersion", "AND-P-" + android.os.Build.VERSION.RELEASE)
                .addHeader("channel", "")
                .addHeader("channel_no", "");

//        if (!NO_TOKEN.contains(url))
//        {
            builder.addHeader("Authorization", "Bearer" + token)
                    .addHeader("access_token", token);
//        }
        builder.build().execute(callback, flag);
    }

    public <E> void post(String url, E requestParam, String flag)
    {
        PostStringBuilder builder = OkHttpUtils
                .postString()
                .tag(tag)
                .url(BaseUrl.baseUrl + url)
                .content(JsonUtils.class2Json(requestParam))
                .addHeader("Connection", "close")
                .addHeader("APPVersion", "AND-P-" + appVersion)
                .addHeader("DeviceModel", "AND-P-" + android.os.Build.MODEL)
                .addHeader("DeviceResolution", "")
                .addHeader("SysVersion", "AND-P-" + android.os.Build.VERSION.RELEASE)
                .addHeader("channel", "")
                .addHeader("channel_no", "");
//        if (!NO_TOKEN.contains(url))
//        {
            builder.addHeader("Authorization", "Bearer" + token)
                    .addHeader("access_token", token);
//        }
//        if ("不需要重试")
//        {
//            builder.build(false).execute(callback, flag);
//        } else
//        {
            builder.build().execute(callback, flag);
//        }
    }
}
