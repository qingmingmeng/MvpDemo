package com.example.zj.mvpdemo.network.okHttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OkCallback<T>
{
    /**
     * UI Thread
     *
     * @param request
     */
	
	public HttpCallback httpCallBack;
	public String className_;
	public Object beanName_;
	public void setResultBeanName(Object  beanName)
	{
		this.beanName_=beanName;
	}
	public void setResultClassName(String className)
	{
		this.className_=className;
	}
	
	public  void SetHttpCallBack(HttpCallback httpCallBack)
	{
		this.httpCallBack=httpCallBack;
	}

    public void onBefore(Request request)
    {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter()
    {

    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress)
    {

    }
    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onError(Call call, Exception e, String flag);

    public abstract void onResponse(T response, String flag);


    public static OkCallback CALLBACK_DEFAULT = new OkCallback()
    {

        @Override
        public Object parseNetworkResponse(Response response) throws Exception
        {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, String flag)
        {

        }

        @Override
        public void onResponse(Object response, String flag)
        {

        }
    };

}