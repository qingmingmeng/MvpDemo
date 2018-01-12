package com.example.zj.mvpdemo.network.okHttp.callback;
/** 
 **********************
 * HttpCallback.java
 * package com.example.ok;
 * com.example.ok
 * 
 * sunqiujing
 * 2016-4-29
 * public class HttpCallback{ }
 * HttpCallback
 *************************
 */
public interface HttpCallback {
	void onNoConnection(String flag);
	void onSuccess(String response, String flag);
	void onErr(String error, String flag);
}
