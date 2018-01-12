package com.example.zj.mvpdemo.network.okHttp.request;

import com.example.zj.mvpdemo.network.okHttp.OkHttpUtils;
import com.example.zj.mvpdemo.network.okHttp.callback.OkCallback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;

/**
 * Created by sunqiujing on 17/1/10.
 */

public class WithOutRetryOnConnectionRequestCall extends RequestCall {

    public WithOutRetryOnConnectionRequestCall(OkHttpRequest request)
    {
        super(request);
    }

    public Call generateCall(OkCallback callback) {

        request = super.generateRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0)
        {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .retryOnConnectionFailure(false)
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else
        {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;

    }
}
