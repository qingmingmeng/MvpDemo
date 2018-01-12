package com.example.zj.mvpdemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.zj.mvpdemo.R;
import com.example.zj.mvpdemo.base.BaseActivity;
import com.example.zj.mvpdemo.base.IView;
import com.example.zj.mvpdemo.bean.location.LocationResultAddressComponentRtn;
import com.example.zj.mvpdemo.presenter.LocationPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IView {

    @BindView(R.id.tv_city)
    TextView tvCity;

    private LocationPresenter locationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        showProgress(true);
        locationPresenter = new LocationPresenter(this);
        locationPresenter.requestLocation();
    }

    @Override
    public void onSuccess(Object response, String flag) {
        super.onSuccess(response, flag);
        showProgress(false);
        if (LocationPresenter.class.getSimpleName().equals(flag)) {
            Log.e("location", "定位成功");
            LocationResultAddressComponentRtn rtn = (LocationResultAddressComponentRtn) response;
            tvCity.setText(rtn.city);
        }
    }

    @Override
    public void onErr(String retFlag, Object response, String flag) {
        showProgress(false);
        if (LocationPresenter.class.getSimpleName().equals(flag)) {
            Log.e("location", "定位失败");
        }
        super.onErr(retFlag, response, flag);
    }

    @Override
    protected void destroyPresenter() {
        if (locationPresenter != null)
            locationPresenter.onDettached();
    }
}
