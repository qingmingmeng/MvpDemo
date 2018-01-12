package com.example.zj.mvpdemo.network.okHttp.request;

import com.example.zj.mvpdemo.network.okHttp.OkHttpUtils;
import com.example.zj.mvpdemo.network.okHttp.callback.OkCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 */
public class RequestCall
{
   protected OkHttpRequest okHttpRequest;
    protected Request request;
    protected Call call;

    protected long readTimeOut;
    protected long writeTimeOut;
    protected long connTimeOut;

    protected OkHttpClient clone;


    public RequestCall(OkHttpRequest request)
    {
        readTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        writeTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        connTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);

        this.okHttpRequest = request;

    }

    public RequestCall readTimeOut(long readTimeOut)
    {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut)
    {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut)
    {
        this.connTimeOut = connTimeOut;
        return this;
    }


    public Call generateCall(OkCallback callback, String flag)
    {
        request = generateRequest(callback);

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

    protected Request generateRequest(OkCallback callback)
    {
        return okHttpRequest.generateRequest(callback);
    }

    public void execute(OkCallback callback, String flag)
    {
        generateCall(callback, flag);

        if (callback != null)
        {
            callback.onBefore(request);
        }

        OkHttpUtils.getInstance().execute(this, callback, flag);
    }

    public Call getCall()
    {
        return call;
    }

    public Request getRequest()
    {
        return request;
    }

    public OkHttpRequest getOkHttpRequest()
    {
        return okHttpRequest;
    }

    public Response execute() throws IOException
    {
        generateCall(null, null);
        return call.execute();
    }

    public void cancel()
    {
        if (call != null)
        {
            call.cancel();
        }
    }


}
