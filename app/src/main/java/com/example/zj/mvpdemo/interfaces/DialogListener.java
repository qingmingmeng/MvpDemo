package com.example.zj.mvpdemo.interfaces;

import android.app.AlertDialog;
import android.widget.TextView;

/**
 * 仅用于弹框提示时(有确定取消按钮)
 * Created by Administrator on 2017/6/1.
 */

public interface DialogListener {
    void dialogCallBack(AlertDialog dialog, TextView left, TextView right);
}
