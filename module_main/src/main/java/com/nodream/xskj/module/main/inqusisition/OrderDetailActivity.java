package com.nodream.xskj.module.main.inqusisition;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.nodream.xskj.module.main.work.model.TaskBean;
import com.nodream.xskj.module.main.work.model.WorkService;
import com.nodream.xskj.module.main.work.presenter.WorkDetailPresenter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2018/08/21.
 */

@Route(path = "/main/orderdetailactivity")
public class OrderDetailActivity extends BaseActivity<AddWorkContract.View, WorkDetailPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private TextView mOrderPatientName;
    private TextView mOrderPatientDetail;
    private TextView mOrderAddress;
    private EditText mOrderDetail;
    private TextView mOrderLinkman;
    private TextView mOrderLinkMobile;
    private TextView mOrderStart;
    private TextView mLocationB;
    private TextView mLocation;

    @Autowired
    public String orderId;
    private String lng;
    private String lat;

    private int taskStatus = 0; //0-初始状态；1-未开始；2-已开始；3-已结束

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
        setContentView(R.layout.activity_order_detail);
        initLocation();
        initView();
//        ToastUtil.showToast(this, orderId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected WorkDetailPresenter createPresenter() {
        return new WorkDetailPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("问诊详情");
        mSimpleToolbar.setLeftImgVisible();
        mSimpleToolbar.setLeftImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDetailActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightGone();

        mOrderPatientName = findViewById(R.id.order_detail_patient_name);
        mOrderPatientDetail = findViewById(R.id.order_detail_patient_detail);

        mOrderAddress = findViewById(R.id.order_detail_address_name);
        mOrderDetail = findViewById(R.id.order_detail_et);
        mOrderLinkman = findViewById(R.id.order_detail_contact);
        mOrderLinkMobile = findViewById(R.id.order_detail_link_phone);
        mOrderStart = findViewById(R.id.order_detail_start);
        mOrderStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeOrder(OrderDetailActivity.this);
            }
        });

        mLocation = findViewById(R.id.order_detail_location);
        mLocationB = findViewById(R.id.work_detail_location_b);
        mLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动定位
                mLocationClient.startLocation();
            }
        });

        getOrderDetail(this);

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(OrderDetailActivity.this);
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
//        mLocationClient.startLocation();
    }

    public void getOrderDetail(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("orderId", orderId);
        NetClient.getInstance(context).create(OrderService.class)
                .getOrderDetail("order/detail",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OrderBean>(context) {
                    @Override
                    protected void onSuccess(OrderBean taskBean) {
                        mOrderPatientName.setText(taskBean.getPatient().getName());
                        if (taskBean.getOrderStatus() != 1) {
                            mOrderDetail.setEnabled(false);
                            mOrderDetail.setTextColor(0xFF333333);
                            mOrderStart.setVisibility(View.GONE);
                            mLocationB.setVisibility(View.GONE);
                        } else {
                            mLocationClient.startLocation();
                        }
                        mOrderDetail.setText(taskBean.getIllnessDesc());
                        mOrderLinkman.setText(taskBean.getLinkman());
                        mOrderLinkMobile.setText(taskBean.getLinkMobile());
                        mLocation.setText(taskBean.getServeLocation().getAddress());
                    }
                });

    }

    public void completeOrder(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("illnessDesc", mOrderPatientDetail.getText().toString());
        map.put("lng", lng);
        map.put("lat", lat);
        NetClient.getInstance(context).create(OrderService.class)
                .completeOrder("order/inquiry",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        ToastUtil.showToast(OrderDetailActivity.this, "问诊成功");
                    }
                });
    }
}
