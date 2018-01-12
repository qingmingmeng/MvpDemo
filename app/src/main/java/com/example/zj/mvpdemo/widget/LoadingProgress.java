package com.example.zj.mvpdemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zj.mvpdemo.R;

/**
 * Created by Administrator on 2017/6/1.
 */

public class LoadingProgress extends Dialog {
    public LoadingProgress(Context context) {
        super(context);
    }

    public LoadingProgress(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    public static LoadingProgress show(Context context, CharSequence message, boolean cancelable,
                                       OnCancelListener cancelListener) {
        LoadingProgress dialog = new LoadingProgress(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setVisibility(View.VISIBLE);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void setMsg(String message) {
        if (message == null || message.length() == 0) {
            this.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) this.findViewById(R.id.message);
            txt.setVisibility(View.VISIBLE);
            txt.setText(message);
        }
    }
}
