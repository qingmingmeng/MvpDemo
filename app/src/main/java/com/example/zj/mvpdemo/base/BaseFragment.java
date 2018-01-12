package com.example.zj.mvpdemo.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.zj.mvpdemo.interfaces.DialogListener;
import com.example.zj.mvpdemo.network.NetResultCallBack;
import com.example.zj.mvpdemo.widget.LoadingProgress;

import butterknife.Unbinder;

/**
 * 项目名称：修改baseFragment
 * 项目作者：胡玉君
 * 创建日期：2016/3/21 19:41.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * activity可以被所有继承者直接访问到
 * ----------------------------------------------------------------------------------------------------
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,NetResultCallBack {

    protected BaseActivity activity;
    protected Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("打开fragment", getFragmentName());
        activity = (BaseActivity) getActivity();
        /**
         * 适配小米和魅族沉浸式状态栏
         */
        /*if ( OSUtils.getRomType()== OSUtils.ROM_TYPE.FLYME)
            FitStateUI.setMeizuStatusBarDarkIcon(activity,true);
        if ( OSUtils.getRomType()== OSUtils.ROM_TYPE.EMUI)
            FitStateUI.setImmersionStateMode(activity);
            FitStateUI.initStatusBar(activity);
//            FitStateUI.setStatusBarFontIconDark(true,getActivity());
        if (OSUtils.getRomType()== OSUtils.ROM_TYPE.MIUI)
            FitStateUI.setStatusBarFontIconDark(true,getActivity());
//            FitStateUI.setMiuiStatusBarDarkMode(activity,true);
        if(OSUtils.getRomType()== OSUtils.ROM_TYPE.OTHER)
            FitStateUI.setImmersionStateMode(activity);
            FitStateUI.initStatusBar(activity);*/
    }

    public void showDialog(String msg) {
        if(activity != null)
            activity.showDialog(msg);
    }

    public void showDialog(String msg, DialogListener listener) {
        if(activity != null)
            activity.showDialog(msg, listener);
    }

    /*展示网络请求菊花*/
    public LoadingProgress progress;
    public void showProgress(boolean flag) {
        if(activity != null)
            showProgress(flag, "请稍候...");
    }
    public void showProgress(boolean flag, String msg) {
        try {
            if (flag && activity != null) {
                if (progress == null) {
                    progress = LoadingProgress.show(activity, msg, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            activity.onBackPressed();
                        }
                    });
                }
                if (progress != null && !progress.isShowing()) {
                    progress.setMsg(msg);
                    progress.show();
                }
            } else {
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            if(progress != null){
                progress.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        progress = null;
        if(null != unbinder)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        destroyPresenter();
        super.onDestroy();
        this.activity = null;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void onSuccess(Object response, String flag) {}

    @Override
    public void onErr(String retFlag, Object response, String flag) {
        showProgress(false);
        showDialog(response.toString());
    }

    /** 用于销毁Presenter */
    protected abstract void destroyPresenter();

    /**
     * fragment name
     */
    public  String getFragmentName(){
        return this.getClass().getSimpleName();
    }
}
