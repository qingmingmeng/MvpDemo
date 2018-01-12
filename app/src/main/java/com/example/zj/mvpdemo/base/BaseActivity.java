package com.example.zj.mvpdemo.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.zj.mvpdemo.R;
import com.example.zj.mvpdemo.interfaces.DialogListener;
import com.example.zj.mvpdemo.network.NetResultCallBack;
import com.example.zj.mvpdemo.widget.LoadingProgress;

import butterknife.Unbinder;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener, NetResultCallBack
{
    protected Unbinder unbinder;
    public static Context baseContext;

    //子类重写该方法以不锁定横竖屏
    protected boolean isPortrait()
    {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.e("进入Activity", this.getClass().getSimpleName());
        AppApplication.activities.add(this);//将activity加入统一管理类
        if (isPortrait())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏写死
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        /** 该行代码防止软键盘弹出将底部View向上顶 */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        baseContext = this;
    }

    @Override
    protected void onDestroy()
    {
        if (adToast != null)
        {
            adToast.dismiss();
        }
        if (adSelect != null)
        {
            adSelect.dismiss();
        }
        if (null != unbinder)
            unbinder.unbind();
        AppApplication.activities.remove(this);
        try
        {
            destroyPresenter();
        } catch (Exception e)
        {
        }
        adToast = null;
        adToastView = null;
        tvToast = null;
        tvUnderstand = null;
        adSelect = null;
        adSelectView = null;
        tvCancel = null;
        tvConfirm = null;
        tvContainer = null;
        super.onDestroy();
        System.gc();
    }

    private AlertDialog adToast;
    private View adToastView;
    private TextView tvToast;
    private TextView tvUnderstand;

    /**
     * 展示一个Dialog提示，每个页面使用一个AlertDialog，防止多层覆盖
     * <p/>
     * 该dialog为提示dialog
     *
     * @param msg 提示内容
     */
    public void showDialog(String msg)
    {
        try
        {
            if (adToast == null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                adToast = builder.create();
            }
            if (adToastView == null)
            {
                adToastView = View.inflate(this, R.layout.dialog_toast, null);
            }
            if (tvToast == null)
            {
                tvToast = (TextView) adToastView.findViewById(R.id.tv_container);
            }
            if (tvUnderstand == null)
            {
                tvUnderstand = (TextView) adToastView.findViewById(R.id.tv_confirm);
                tvUnderstand.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        adToast.dismiss();
                    }
                });
            }

            tvToast.setText(msg);
            adToast.show();

            Window window = adToast.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            Display display = getWindowManager().getDefaultDisplay();
            params.width = (int) (display.getWidth() * 0.7);
            window.setAttributes(params);

            adToast.setContentView(adToastView);
            adToast.setCanceledOnTouchOutside(false);
        } catch (Exception e)
        {
            if (adToast != null)
            {
                adToast.dismiss();
                adToast = null;
            }
            e.printStackTrace();
        }
    }

    private AlertDialog adSelect;
    private View adSelectView;
    private TextView tvContainer;
    private TextView tvConfirm;
    private TextView tvCancel;

    /**
     * 展示一个带有两个选择按钮的提示Dialog
     *
     * @param msg      内容
     * @param listener 回调，不能为null
     */
    public void showDialog(String msg, DialogListener listener)
    {
        try
        {
            if (adSelect == null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                adSelect = builder.create();
            }
            if (adSelectView == null)
            {
                adSelectView = View.inflate(this, R.layout.dialog_warn, null);
            }
            if (tvContainer == null)
                tvContainer = (TextView) adSelectView.findViewById(R.id.tv_container);
            if (tvConfirm == null)
                tvConfirm = (TextView) adSelectView.findViewById(R.id.tv_confirm);
            if (tvCancel == null)
                tvCancel = (TextView) adSelectView.findViewById(R.id.tv_cancel);
            tvContainer.setText(msg);
            tvConfirm.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            listener.dialogCallBack(adSelect, tvCancel, tvConfirm);
            adSelect.show();

            Window window = adSelect.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            Display display = getWindowManager().getDefaultDisplay();
            params.width = (int) (display.getWidth() * 0.7);
            window.setAttributes(params);

            adSelect.setContentView(adSelectView);
            adSelect.setCanceledOnTouchOutside(false);
        } catch (Exception e)
        {
            if (adSelect != null)
            {
                adSelect.dismiss();
                adSelect = null;
            }
            e.printStackTrace();
        }
    }

    /*展示网络请求等待的菊花*/
    public LoadingProgress progress;

    public void showProgress(boolean flag)
    {
        showProgress(flag, "");
    }

    public void showProgress(boolean flag, String msg)
    {
        try
        {
            if (flag)
            {
                if (progress == null)
                {
                    progress = LoadingProgress.show(this, msg, true, new DialogInterface.OnCancelListener()
                    {
                        @Override
                        public void onCancel(DialogInterface dialog)
                        {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });
                }
                if (progress != null)
                {
                    progress.setMsg(msg);
                    if (!progress.isShowing())
                        progress.show();
                }
            } else
            {
                if (progress != null && progress.isShowing())
                {
                    progress.dismiss();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        Log.e("内存回收", this.getClass().getSimpleName() + "被回收了");
        super.finalize();
    }

    @Override
    public void onClick(View v)
    {
    }

    @Override
    public void onSuccess(Object response, String flag)
    {
    }

    @Override
    public void onErr(String retFlag, Object response, String flag)
    {
        showProgress(false);
        showDialog(response.toString());
    }

    /**
     * 用于销毁presenter
     */
    protected abstract void destroyPresenter();

    @Override
    public void finish()
    {
        showProgress(false);
        if (adSelect != null && adSelect.isShowing())
        {
            adSelect.dismiss();
        }
        if (adToast != null && adToast.isShowing())
        {
            adToast.dismiss();
        }
        super.finish();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    /*点击空白区域时，隐藏软键盘*/
    public boolean onTouchEvent(MotionEvent event)
    {
        if (null != this.getCurrentFocus())
        {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
