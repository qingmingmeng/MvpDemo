package com.example.zj.mvpdemo.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.zj.mvpdemo.base.AppApplication;
import com.example.zj.mvpdemo.base.BasePresenter;
import com.example.zj.mvpdemo.base.IView;
import com.example.zj.mvpdemo.bean.location.LocationResultAddressComponentRtn;
import com.example.zj.mvpdemo.network.model.LocationModel;
import com.example.zj.mvpdemo.network.okHttp.callback.StringCallback;
import com.example.zj.mvpdemo.utils.CheckUtil;

import java.lang.ref.WeakReference;

import okhttp3.Call;

/**
 * 项目名称：定位逻辑处理
 * 项目作者：胡玉君
 * 创建日期：2017/6/6 18:01.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 * <p>
 * 定位的flag全部为该类的全类名 LocationPresenter.class.getSimpleName();
 * <p>
 * 请求定位前必须获取的权限是
 * 定位权限
 * 读写SD卡权限
 * <p>
 * 只要走到onSuccess()即有效
 * <p>
 * <p>
 * 定位code码
 * //以下为定位成功
 * 61：GPS定位结果，GPS定位成功。
 * 161：网络定位结果，网络定位成功。
 * 66：离线定位成功。通过requestOfflineLocaiton调用时对应的返回结果。
 * 65：定位缓存的成功。
 * <p>
 * //初始化失败
 * V0201：因某些问题而导致的异常(cash)情况
 * <p>
 * //以下为定位失败
 * 62：无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位。
 * 63：网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
 * 64: 服务器返回数据异常，根据经纬度请求省市编码失败
 * 167：服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
 * <p>
 * //以下基本不会返回
 * 67：离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
 * 68：网络连接失败时，查找本地离线定位时对应的返回结果。
 * <p>
 * //以下为开发者错误开发导致失败
 * 162：请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
 * 502：AK参数错误，请按照说明文档重新申请AK。
 * 505：AK不存在或者非法，请按照说明文档重新申请AK。
 * 601：AK服务被开发者自己禁用，请按照说明文档重新申请AK。
 * 602：key mcode不匹配，您的AK配置过程中安全码设置有问题，请确保：SHA1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请AK。
 * 501～700：AK验证失败，请按照说明文档重新申请AK。
 * ----------------------------------------------------------------------------------------------------
 */

public class LocationPresenter extends BasePresenter<IView> {

    private LocationClient mLocationClient;
    private BDLocationListener myListener;
    private LocationCallBack callBack;
    private LocationHandler handler;

    public static final String ERROR_INIT = "V0201";//定位SDK初始化失败,可能原因：无相关权限权限

    public LocationPresenter(IView view) {
        super(view);
        myListener = new MyLocationListener(this);
        callBack = new LocationCallBack(this);
        handler = new LocationHandler(this);
    }

    public void requestLocation() {
        if (viewIsNull()){
            return;
        }
        try {
            // 声明LocationClient类
            mLocationClient = new LocationClient(AppApplication.CONTEXT);
            mLocationClient.registerLocationListener(myListener); // 注册监听函数
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(0);// 设置发起定位请求的间隔时间为5000ms
            option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
            option.setOpenGps(true);
            option.disableCache(true);// 禁止启用缓存定位
            option.setIsNeedLocationDescribe(true);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            view.onErr(ERROR_INIT, "定位失败，请开启定位权限"+ERROR_INIT, LocationPresenter.class.getSimpleName());
        }
    }

    //位置监听器
    private static final class MyLocationListener implements BDLocationListener {

        WeakReference<LocationPresenter> reference;

        public MyLocationListener(LocationPresenter presenter) {
            reference = new WeakReference<>(presenter);
        }
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || reference == null || reference.get() == null) return;
            if (reference.get().mLocationClient != null) {
                reference.get().mLocationClient.stop();
                reference.get().mLocationClient.unRegisterLocationListener(reference.get().myListener);
            }
            Message msg = Message.obtain();
            msg.obj = location;
            reference.get().handler.sendMessage(msg);
        }
    }

    private static final class LocationCallBack extends StringCallback {

        private WeakReference<LocationPresenter> reference;
        public LocationCallBack(LocationPresenter presenter) {
            reference = new WeakReference<>(presenter);
        }

        @Override
        public void onError(Call call, Exception e, String flag) {
            if (reference == null || reference.get() == null) return;
            if (reference.get().viewIsNull()){
                return;
            }
            if (e.toString().contains("Canceled") || e.toString().contains("Socket closed")) {
                return;
            }
            if (reference != null && reference.get() != null) {
                reference.get().view.onErr("63", "定位失败，请检查网络是否通畅(63)", LocationPresenter.class.getSimpleName());
          }
        }

        @Override
        public void onResponse(String response, String flag) {
            Log.e("定位", response);
            if (reference == null || reference.get() == null) return;
            if (reference.get().viewIsNull()){
                return;
            }
            if (reference != null && reference.get() != null) {
                if (CheckUtil.isEmpty(response)) {
                    reference.get().view.onErr("64", "服务器返回数据异常(64)", LocationPresenter.class.getSimpleName());
                } else {
                    Object result = new LocationModel(null).parseResponse(response);
                    if (result instanceof LocationResultAddressComponentRtn) {
                        reference.get().view.onSuccess(result, LocationPresenter.class.getSimpleName());
                    } else {
                        reference.get().view.onErr("64", "服务器返回数据异常(64)", LocationPresenter.class.getSimpleName());
                    }
                }
            }
        }
    }

    private static final class LocationHandler extends Handler {

        WeakReference<LocationPresenter> reference;

        public LocationHandler(LocationPresenter presenter) {
            reference = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            if (reference == null || reference.get() == null ) return;
            if (reference.get().viewIsNull()){
                return;
            }
            if (msg != null && msg.obj != null && msg.obj instanceof BDLocation) {
                BDLocation location = (BDLocation) msg.obj;
                if (location.getLocType() == 167) {
                    reference.get().view.onErr("167", "定位失败，请开启定位权限(167)", LocationPresenter.class.getSimpleName());
                } else if (location.getLocType() == 63) {
                    reference.get().view.onErr("63", "定位失败，请检查网络是否通畅(63)", LocationPresenter.class.getSimpleName());
                } else if (location.getLocType() == 62) {
                    reference.get().view.onErr("62", "定位失败，请确保网络通畅并开启了GPS(62)", LocationPresenter.class.getSimpleName());
                } else {
                    // 获取定位的经纬度
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    if ("4.9E-324".equals(String.valueOf(lat)) || "4.9E-324".equals(String.valueOf(lon))) {
                        reference.get().view.onErr(String.valueOf(location.getLocType()), "当前无法确定您的位置，请移至开阔地带并开启定位服务("+location.getLocType()+")", LocationPresenter.class.getSimpleName());
                    } else {
//                        lon = 109.4;lat=19;
                        if (lat > 0 && lon > 0) {
                            new LocationModel(reference.get()).requestLocation(lat, lon, reference.get().callBack);
                        } else {
                            reference.get().view.onErr(String.valueOf(location.getLocType()), "当前无法确定您的位置，请移至开阔地带并开启定位服务", LocationPresenter.class.getSimpleName());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDettached() {
        if (mLocationClient != null && myListener != null) {
            mLocationClient.unRegisterLocationListener(myListener);
        }
        mLocationClient = null;
        super.onDettached();
    }


}
