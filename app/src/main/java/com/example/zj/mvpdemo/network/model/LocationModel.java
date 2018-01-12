package com.example.zj.mvpdemo.network.model;

import com.example.zj.mvpdemo.base.BaseModel;
import com.example.zj.mvpdemo.bean.location.LocationResultAddressComponentRtn;
import com.example.zj.mvpdemo.bean.location.LocationRtn;
import com.example.zj.mvpdemo.network.NetResultCallBack;
import com.example.zj.mvpdemo.network.okHttp.OkHttpUtils;
import com.example.zj.mvpdemo.network.okHttp.callback.OkCallback;
import com.example.zj.mvpdemo.presenter.LocationPresenter;
import com.example.zj.mvpdemo.utils.CheckUtil;
import com.example.zj.mvpdemo.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：HFAndroid
 * 项目作者：胡玉君
 * 创建日期：2017/6/7 10:59.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 *
 * 返回值为
 * String
 * LocationResultAddressComponentRtn
 * ----------------------------------------------------------------------------------------------------
 */

public class LocationModel extends BaseModel {

    public LocationModel(NetResultCallBack callBack) {
        super(callBack);
    }

    public void requestLocation(double lat, double lon, OkCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("ak", "WzqruAlVr1AcpSR5sh1HOGUaB5EQNFsH");
        params.put("callback", "renderReverse");
        params.put("location", lat + "," + lon);
        params.put("output", "json");
        params.put("pois", "1");
        params.put("mcode", "2F:9D:05:46:3F:E5:69:01:7B:65:32:5E:D5:23:2E:BD:19:92:37:C4;com.example.zj.mvpdemo");
        OkHttpUtils.get()
                .tag(callBack())
                .url("http://api.map.baidu.com/geocoder/v2/")
                .params(params)
                .addHeader("Connection", "close")
                .build()
                .execute(callback, LocationPresenter.class.toString());
    }

    @Override
    public Object parseResponse(String response) {
        if (response.contains("renderReverse&&renderReverse(")) {
            response = response.replace("renderReverse&&renderReverse(", "");
            response = response.substring(0, response.length() - 1);
            LocationRtn location = JsonUtils.json2Class(response, LocationRtn.class);
            if(location == null || CheckUtil.isEmpty(location.status)){
                return response;
            } else {
                LocationResultAddressComponentRtn result = location.result.addressComponent;
                String cityName = result.city;
                String cityCode = result.cityCode;
                return result;
            }
        }
        return response;
    }
}
