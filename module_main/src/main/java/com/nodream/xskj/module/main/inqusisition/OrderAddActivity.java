package com.nodream.xskj.module.main.inqusisition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.utils.ProgressDialogUtil;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.inqusisition.model.OrderBean;
import com.nodream.xskj.module.main.inqusisition.model.OrderService;
import com.nodream.xskj.module.main.work.AddWorkContract;
import com.nodream.xskj.module.main.work.presenter.WorkDetailPresenter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2018/08/21.
 */

@Route(path = "/main/orderaddactivity")
public class OrderAddActivity extends BaseActivity<AddWorkContract.View, WorkDetailPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private RelativeLayout mOrderPatientRl;
    private TextView mOrderPatientName;
    private EditText mOrderDetail;
    private EditText mOrderLinkman;
    private EditText mOrderLinkMobile;
    private TextView mLocationB;
    private TextView mLocation;

    private int patientId = -1;
    private String lng;
    private String lat;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    Logger.i("locationAddress:",amapLocation.getAddress());
                    Logger.i("location纬度:",amapLocation.getLatitude());
                    Logger.i("location经度:",amapLocation.getLongitude());
                    lng = String.valueOf(amapLocation.getLongitude());
                    lat = String.valueOf(amapLocation.getLatitude());
                    //可在其中解析amapLocation获取相应内容。
                    mLocation.setText(amapLocation.getAddress());
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    ToastUtil.showToast(OrderAddActivity.this, "定位失败，请重新定位！");
                }
            }
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_order_add);
        initView();
        initLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Logger.e("data == null");
            return;
        }
        if (requestCode == 200 && resultCode == 0) {
            patientId = data.getIntExtra("id", -1);
            String name = data.getStringExtra("name");
            mOrderPatientName.setText(name);
        }
    }

    @Override
    protected WorkDetailPresenter createPresenter() {
        return new WorkDetailPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("添加问诊");
        mSimpleToolbar.setLeftImgVisible();
        mSimpleToolbar.setLeftImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderAddActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightTitleText("完成");
        mSimpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams();
//                completeOrder(OrderAddActivity.this);
            }
        });

        mOrderPatientRl = findViewById(R.id.order_add_patient_name_rl);
        mOrderPatientRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/patientselectactivity")
                        .navigation(OrderAddActivity.this, 200);
            }
        });
        mOrderPatientName = findViewById(R.id.order_add_patient_name);
        mOrderDetail = findViewById(R.id.order_detail_et);
        mOrderLinkman = findViewById(R.id.order_detail_contact);
        mOrderLinkMobile = findViewById(R.id.order_detail_link_phone);

        mLocationB = findViewById(R.id.work_detail_location_b);
        mLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动定位
                mLocationClient.startLocation();
            }
        });
        mLocation = findViewById(R.id.order_detail_location);

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(OrderAddActivity.this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void checkParams() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(mOrderDetail.getText().toString())) {
            mOrderDetail.setError("病情描述不能为空");
            cancel = true;
            focusView = mOrderDetail;
        } else if (TextUtils.isEmpty(mOrderLinkman.getText().toString())) {
            mOrderLinkman.setError("联系人不能为空");
            cancel = true;
            focusView = mOrderLinkman;
        } else if (TextUtils.isEmpty(mOrderLinkMobile.getText().toString())) {
            mOrderLinkMobile.setError("联系人电话不能为空");
            cancel = true;
            focusView = mOrderLinkMobile;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            completeOrder(this);
        }
    }

    public void completeOrder(Context context) {
        Map<String,String> map = new HashMap<>();
        Logger.e(patientId + "");
        if (patientId != -1) {
            map.put("patientId", Integer.toString(patientId));
        } else {
            ToastUtil.showToast(OrderAddActivity.this, "请选择病人");
            return;
        }
        map.put("illnessDesc", mOrderDetail.getText().toString());
        map.put("linkman", mOrderLinkman.getText().toString());
        map.put("linkMobile", mOrderLinkMobile.getText().toString());
        map.put("serveLocation", mLocation.getText().toString());
        if (lng != null && !lng.equals("")) {
            map.put("lng", lng);
            map.put("lat", lat);
        } else {
            ToastUtil.showToast(OrderAddActivity.this, "请重新定位");
            return;
        }
        ProgressDialogUtil.showProgressDialog(context);
        NetClient.getInstance(context).create(OrderService.class)
                .completeOrder("order/create",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        ToastUtil.showToast(OrderAddActivity.this, "添加成功");
                        OrderAddActivity.this.finish();
                    }
                });
    }
}
