package com.example.zj.mvpdemo.network.okHttp.request;

import com.example.zj.mvpdemo.network.okHttp.utils.Exceptions;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 功能：
 * 创建：sunqiujing
 * 日期: on 16/5/13 10:11
 */
public class DeleteStringRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("application/json;charset=utf-8");

    private String content;
    private MediaType mediaType;
    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.delete(requestBody).build();
    }
    public DeleteStringRequest  (String url, Object tag, Map<String, String> params, Map<String, String> headers)
    {
        super(url, tag, params, headers);
    }


    public DeleteStringRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String content, MediaType mediaType) {

        super(url, tag, params, headers);

        this.content = content;
        this.mediaType = mediaType;

        if (this.content == null)
        {
            Exceptions.illegalArgument("the content can not be null !");
        }
        if (this.mediaType == null)
        {
            this.mediaType = MEDIA_TYPE_PLAIN;
        }

    }


    @Override
    public String toString() {
        return super.toString() + ", requestBody{content=" + content + "} ";
    }
}
