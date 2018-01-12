package com.example.zj.mvpdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.zj.mvpdemo.R;
import com.example.zj.mvpdemo.base.AppApplication;
import com.example.zj.mvpdemo.interfaces.SaveImageResult;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;


/**
 * 项目名称：UI
 * 项目作者：胡玉君
 * 创建日期：2016/3/11 17:48.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * showToast 自定义toast样式
 * <p>
 * 隐藏、显示菜单(伴随动画效果)
 * hide_top_menu
 * show_top_menu
 * <p>
 * 底部隐藏、弹出菜单(伴随动画效果)
 * hide_menu
 * show_menu
 * <p>
 * dip2px dp转为px
 * getStatusBarHeight 获取状态栏高度
 * ----------------------------------------------------------------------------------------------------
 */
public class UiUtil {

    /**
     * 单例Toast
     * <p>
     * toast禁止用于网络请求返回时的提示，因为用户可能看不到
     */
    public static Toast mToast;

    public static void toast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(AppApplication.CONTEXT, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    /**
     * dip 转换成 px
     *
     * @param dip
     * @return
     */
    public static float dip2px(float dip) {
        DisplayMetrics displayMetrics = AppApplication.CONTEXT.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }

    /**
     *  透明导航栏 防止虚拟键盘顶起 压缩图片
     */
    public static void TransparentNavigationBar(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * @param dip
     * @param complexUnit {@link TypedValue#COMPLEX_UNIT_DIP} {@link TypedValue#COMPLEX_UNIT_SP}}
     * @return
     */
    public static float toDimension(float dip, int complexUnit) {
        DisplayMetrics displayMetrics = AppApplication.CONTEXT.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(complexUnit, dip, displayMetrics);
    }

    /**
     * 获取状态栏高度
     *
     * @param v
     * @return
     */
    public static int getStatusBarHeight(View v) {
        if (v == null) {
            return 0;
        }
        Rect frame = new Rect();
        v.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static void hideKeyBord(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            ((InputMethodManager) AppApplication.CONTEXT.getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取设备标识码
     */
    public static String getDeviceID() {
        String deviceId = "";
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) AppApplication.CONTEXT.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = mTelephonyMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getDeviceWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) AppApplication.CONTEXT.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getDeviceHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) AppApplication.CONTEXT.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    //防止多次点击记录上次点击时间
    private static long lastClickTime;

    /**
     * 是不是快速点击400ms
     * 用于普通按钮防暴击处理
     * @return ture 是 false 不是
     */
    public static boolean isFastDoubleClicks() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 400) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 用于Glide加载图片，将url附加header
     *
     * @param url 下载图片的url地址
     * @return 返回带有header的url
     */
    public static GlideUrl addHeader(String url) {
        //String token = SpHelper.getInstance().readMsgFromSp(SpKey.LOGIN, SpKey.LOGIN_ACCESSTOKEN);
        String appVersion;
        try {
            appVersion = AppApplication.CONTEXT.getPackageManager().getPackageInfo(AppApplication.CONTEXT.getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (Exception e) {
            appVersion = "";
        }
        LazyHeaders headers;
        headers = new LazyHeaders.Builder()
                .addHeader("APPVersion", "AND-P-" + appVersion)
                .addHeader("DeviceModel", "AND-P-" + Build.MODEL)
                .addHeader("DeviceResolution", "AND-P-" + UiUtil.getDeviceWidth() + "," + UiUtil.getDeviceHeight())
                .addHeader("SysVersion", "AND-P-" + Build.VERSION.RELEASE)
                .addHeader("channel", "")
                .addHeader("channel_no", "")
                .addHeader("access_token", "")
                .addHeader("Authorization", "Bearer")
                .build();

        return new GlideUrl(url, headers);
    }

    /**
     * 保存一个图像文件到本地
     * <p>
     * 注意：
     * 1. 文件存储路径不能乱存，如何存储请看AppApplication
     * 2. 调用前请检查SD卡写入权限
     *
     * @param bitmap   图像
     * @param filePath 保存路径，可以不以“/”结尾
     * @param fileName 文件名，可以不以“.jpg”结尾
     * @param result   存储回调，可以为null
     */
    public void saveImageFile(Bitmap bitmap, String filePath, String fileName, SaveImageResult result) {
        if (bitmap == null) {
            if (result != null) {
                result.onSaveFailed("HUNO_NULL", "没有获取到图像信息，保存失败");
            }
            return;
        }
        boolean sdCardWritePermission =
                AppApplication.CONTEXT.getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, AppApplication.CONTEXT.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (!sdCardWritePermission) {
            if (result != null) {
                result.onSaveFailed("HUNO_PERMISSION", "没有sd卡读写权限");
            }
            return;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File imageFile = new File(file.getPath() + "/" + fileName);
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outStream);
                if (result != null) {
                    result.onSaveSuccess(imageFile, fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (result != null) {
                    result.onSaveFailed("HUNO_ERROR", e.getMessage());
                }
            } finally {
                if (outStream != null) {
                    try {
                        outStream.flush();
                        outStream.close();
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            if (result != null) {
                result.onSaveFailed("HUNO_SDCARD_INVALIBLE", "sd卡不可用");
            }
        }
    }

    /**
     * 过滤所有空格
     */
    public static String allWhite(String s) {
        if (s != null && s.contains(" ")) {
            return s.replaceAll(" ", "");
        } else {
            return s;
        }
    }

    /**
     * 获取app版本号，当前仅打印Log用，其他地方需要使用再修改
     */
    public static void getAppVersion() {
        PackageInfo pi = null;
        String appVersion;
        try {
            PackageManager pm = AppApplication.CONTEXT.getPackageManager();
            pi = pm.getPackageInfo(AppApplication.CONTEXT.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            appVersion = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            appVersion = "";
        }
        Log.e("获取手机设备信息>>>>>>>>", "版本号：" + appVersion);
    }

    /**
     * 获取手机版本号，当前仅打印Log用，其他地方需要使用再修改
     */
    public static void getSysVersion() {
        String sysVersion;
        try {
            sysVersion = Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
            sysVersion = "";
        }
        Log.e("获取手机设备信息>>>>>>>>", "手机系统版本号：" + sysVersion);
    }

    /**
     * 获取设备分辨率，当前仅打印Log用，其他地方需要使用再修改
     */
    public static void getDeviceResolution() {
        int width, height;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowMgr = (WindowManager) AppApplication.CONTEXT.getSystemService(Context.WINDOW_SERVICE);
            windowMgr.getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
            height = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            width = 0;
            height = 0;
        }
        Log.e("获取手机设备信息>>>>>>>>", "手机分辨率：" + width + "," + height);
    }

    /**
     * 获取手机型号，当前仅打印Log用，其他地方需要使用再修改
     */
    public static void getDeviceModel() {
        String deviceModel;
        try {
            deviceModel = Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            deviceModel = "";
        }
        Log.e("获取手机设备信息>>>>>>>>", "手机型号：" + deviceModel);
    }

    /**
     * 获取手机分辨率的倍数
     */
    public static String getSizeType(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        return "AND1080";
//        if (1.5 == density) {
//            return "AND480";
//        } else if (2.0 == density) {
//            return "AND720";
//        } else if (3.0 == density) {
//            return "AND1080";
//        } else {
//            return "AND1080";
//        }
    }

    /**
     * 监听输入金额控件，不能以0开头，只能输入小输掉后两位
     */
    public static void getTextWatcher(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String str = editText.getText().toString();
                if (str.length() == 1) {
//                    if ("0".equals(s.toString())) {
//                        toast(AppApplication.CONTEXT.getString(R.string.common_money_str));
//                        editText.setText("");
//                    }
                }
                if (str.length() == 1) {
                    if (".".equals(s.toString())) {
                        editText.setText("");
                        return;
                    }
                }
                try {
                    CheckUtil.counter = 0;
                    if (CheckUtil.countStr(str, ".") > 1) {
                        editText.setText(str.substring(0, str.length() - 1));
                        editText.setSelection(editText.getText().length());//光标放在文本最后面
                    }
                    //限制只能输入小数点后2位
                    if (str.contains(".") || str.startsWith("0.")) {
                        String str1 = str.substring(str.lastIndexOf(".") + 1);
                        if (str1.length() > 2) {
                            editText.setText(str.substring(0, str.lastIndexOf(".") + 3));
                        }
//                        editText.setSelection(editText.getText().length());//光标放在文本最后面
                    }
                    if (str.startsWith("00")) {
                        editText.setText("0.00");
                    }
                } catch (Exception e) {
                    Log.e("--", e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 监听输入金额控件，可以输入0，只能输入小输掉后两位
     */
    public static void getTextWatcherZero(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String str = editText.getText().toString();
                if (str.length() != 1) {
                    try {
                        String string = str.substring(0, 1);
                        if ("0".equals(string)) {
                            editText.setText("0");
                        }
                    } catch (Exception e) {

                    }
                }
                if (str.length() == 1) {
                    if (".".equals(s.toString())) {
                        editText.setText("");
                        return;
                    }
                }
                try {
                    CheckUtil.counter = 0;
                    if (CheckUtil.countStr(str, ".") > 1) {
                        editText.setText(str.substring(0, str.length() - 1));
                        editText.setSelection(editText.getText().length());//光标放在文本最后面
                    }
                    //限制只能输入小数点后2位
                    if (str.contains(".")) {
                        String str1 = str.substring(str.lastIndexOf(".") + 1);
                        if (str1.length() > 2) {
                            editText.setText(str.substring(0, str.lastIndexOf(".") + 3));
                        }
                        editText.setSelection(editText.getText().length());//光标放在文本最后面
                    }
                } catch (Exception e) {
                    Log.e("--", e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 隐藏从底部弹出的view
     *
     * @param context
     * @param v
     */
    public static void hide_menu(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.push_top_out));
        v.setVisibility(View.GONE);
    }

    /**
     * 从底部弹出view
     *
     * @param context
     * @param v
     */
    public static void show_menu(Context context, View v) {
        if (context == null || v == null) {
            return;
        }
        v.setVisibility(View.VISIBLE);
        v.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.push_top_in));
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 保留scale 位小数
     * @return 两个参数的积 double类型
     */
    public static double mul(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
