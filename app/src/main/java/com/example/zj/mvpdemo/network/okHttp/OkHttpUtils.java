package com.example.zj.mvpdemo.network.okHttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.zj.mvpdemo.network.okHttp.builder.DeleteBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.GetBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PostFileBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PostFormBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PostImageBuider;
import com.example.zj.mvpdemo.network.okHttp.builder.PostStringBuilder;
import com.example.zj.mvpdemo.network.okHttp.builder.PutStringBuilder;
import com.example.zj.mvpdemo.network.okHttp.callback.OkCallback;
import com.example.zj.mvpdemo.network.okHttp.cookie.SimpleCookieJar;
import com.example.zj.mvpdemo.network.okHttp.https.HttpsUtils;
import com.example.zj.mvpdemo.network.okHttp.request.RequestCall;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {


    public static final String TAG = "OkHttpUtils";
    public static final long DEFAULT_MILLISECONDS = 50000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpUtils() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        //cookie enabled
        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        mDelivery = new Handler(Looper.getMainLooper());


        if (true) {
            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }

        mOkHttpClient = okHttpClientBuilder.build();
    }

    private boolean debug;
    private String tag;

    public OkHttpUtils debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }
    public static PostImageBuider postImage() {
        return new PostImageBuider();
    }

    public static PutStringBuilder putString() {
        return new PutStringBuilder();
    }

    public static DeleteBuilder delete()
    {
        return new DeleteBuilder();
    }


    public void execute(final RequestCall requestCall, OkCallback callback, final String flag) {
        if (debug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            Log.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getOkHttpRequest().toString() + "}");
        }

        if (callback == null)
            callback = OkCallback.CALLBACK_DEFAULT;
        final OkCallback finalCallback = callback;

        final Call call = requestCall.getCall();

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailResultCallback(call, e, finalCallback, flag);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback, flag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    Object o = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback, flag);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, flag);
                }
            }
        });


    }

    public void sendFailResultCallback(final Call call, final Exception e, final OkCallback callback, final String flag) {
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, flag);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final OkCallback callback, final String flag) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, flag);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        if(tag == null){
            mOkHttpClient.dispatcher().cancelAll();
            return;
        }
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void setCertificates(InputStream... certificates) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null))
                .build();
    }


    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .connectTimeout(timeout, units)
                .build();
    }
}

