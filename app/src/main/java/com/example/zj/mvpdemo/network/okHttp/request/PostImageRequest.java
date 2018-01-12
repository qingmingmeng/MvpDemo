package com.example.zj.mvpdemo.network.okHttp.request;

import com.example.zj.mvpdemo.network.okHttp.OkHttpUtils;
import com.example.zj.mvpdemo.network.okHttp.builder.PostImageBuider;
import com.example.zj.mvpdemo.network.okHttp.callback.OkCallback;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 功能：
 * 创建：sunqiujing
 * 日期: on 16/5/25 09:34
 */
public class PostImageRequest extends OkHttpRequest {


    private List<PostImageBuider.FileInput> imageList;

    public PostImageRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, List<PostImageBuider.FileInput> files)
    {
        super(url, tag, params, headers);
        this.imageList = files;
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        if (imageList == null || imageList.isEmpty())
        {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        } else
        {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < imageList.size(); i++)
            {
                PostImageBuider.FileInput fileInput = imageList.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final OkCallback callback)
    {
        if (callback == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {

                OkHttpUtils.getInstance().getDelivery().post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody)
    {
        return builder.post(requestBody).build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder)
    {
        if (params != null && !params.isEmpty())
        {
            for (String key : params.keySet())
            {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private void addParams(FormBody.Builder builder)
    {
        if (params == null || params.isEmpty())
        {
            builder.add("1", "1");
            return;
        }

        for (String key : params.keySet())
        {
            builder.add(key, params.get(key));
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (imageList != null)
        {
            for (PostImageBuider.FileInput file : imageList)
            {
                sb.append(file.toString()+"  ");
            }
        }
        return sb.toString();
    }

}
