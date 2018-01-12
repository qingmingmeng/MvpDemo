package com.example.zj.mvpdemo.network.okHttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends OkCallback<String>
{
    @Override
    public String parseNetworkResponse(Response response) throws IOException
    {
        return response.body().string();
    }

}
