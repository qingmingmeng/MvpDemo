package com.example.zj.mvpdemo.base;

import com.example.zj.mvpdemo.interfaces.DialogListener;
import com.example.zj.mvpdemo.network.NetResultCallBack;

/**
 * Created by Administrator on 2017/6/2.
 */
public interface IView extends NetResultCallBack
{
    void showProgress(boolean flag);

    void showProgress(boolean flag, String msg);

    void showDialog(String msg);

    void showDialog(String msg, DialogListener listener);
}
