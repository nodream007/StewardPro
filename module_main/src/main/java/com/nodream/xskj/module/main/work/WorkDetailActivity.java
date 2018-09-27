package com.nodream.xskj.module.main.work;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.nodream.xskj.module.main.work.model.ConsumablesBean;
import com.nodream.xskj.module.main.work.model.TaskBean;
import com.nodream.xskj.module.main.work.model.WorkBean;
import com.nodream.xskj.module.main.work.model.WorkResponse;
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

@Route(path = "/main/workdetailactivity")
public class WorkDetailActivity extends BaseActivity<AddWorkContract.View, WorkDetailPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private TextView mWorkProductName;
    private TextView mWorkDescription;
    private TextView mWorkTime;
    private TextView mWorkLinkman;
    private TextView mWorkLinkMobile;
    private TextView mWorkServeLocation;
    private TextView mWorkStart;
    private TextView mWorkConsumables;
    private TextView mWorkConsumablesB;
    private TextView mLocationB;
    private TextView mLocationT;
    private ImageView mLocationI;
    private TextView mWorkLocation;

    @Autowired
    public String taskId;

    private int taskStatus = 0; //0-初始状态；1-未开始；2-已开始；3-已结束

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
                    //可在其中解析amapLocation获取相应内容。
                    mWorkLocation.setText(amapLocation.getAddress());
                    lng = String.valueOf(amapLocation.getLongitude());
                    lat = String.valueOf(amapLocation.getLatitude());
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
        setContentView(R.layout.activity_work_detail);
        initLocation();
        initView();
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
        mSimpleToolbar.setMainTitle("任务详情");
        mSimpleToolbar.setLeftImgVisible();
        mSimpleToolbar.setLeftImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkDetailActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightGone();

        mWorkProductName = findViewById(R.id.work_detail_product_name);
        mWorkDescription = findViewById(R.id.work_detail_et);
        mWorkTime = findViewById(R.id.work_detail_time);
        mWorkLinkman = findViewById(R.id.work_detail_contact);
        mWorkLinkMobile = findViewById(R.id.work_detail_phone);
        mWorkServeLocation = findViewById(R.id.work_detail_addr);
        mWorkStart = findViewById(R.id.work_detail_start);
        mWorkStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskStatus == 1) {
                    startTask(WorkDetailActivity.this);
                } else if (taskStatus == 2) {
                    completeTask(WorkDetailActivity.this);
                }
            }
        });
        mWorkConsumables = findViewById(R.id.work_detail_consumables);
        mWorkConsumablesB = findViewById(R.id.work_detail_consumables_b);
        mWorkConsumablesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveConsumables(WorkDetailActivity.this);
            }
        });
        mWorkLocation = findViewById(R.id.work_detail_location);
        mLocationB = findViewById(R.id.work_detail_location_b);
        mLocationB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.startLocation();
            }
        });
        mLocationT = findViewById(R.id.work_detail_location_t);
        mLocationI = findViewById(R.id.work_detail_location_i);
        getTaskDetail(this);

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(WorkDetailActivity.this);
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

    public void getTaskDetail(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("taskId", taskId);
        NetClient.getInstance(context).create(WorkService.class)
                .getTaskDetail("task/detail",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<TaskBean>(context) {
                    @Override
                    protected void onSuccess(TaskBean taskBean) {
                        mWorkProductName.setText(taskBean.getProduct().getName());
                        mWorkDescription.setText(taskBean.getDescription());
                        mWorkTime.setText(taskBean.getStartTime());
                        mWorkLinkman.setText(taskBean.getLinkman());
                        mWorkLinkMobile.setText(taskBean.getLinkMobile());
                        mWorkServeLocation.setText(taskBean.getServeLocation().getAddress());
                        for (ConsumablesBean consumablesBean : taskBean.getProduct().getConsumables()) {
                            String con = consumablesBean.getName() + "✖"
                                    + consumablesBean.getNum() + "\n";
                            mWorkConsumables.setText(con);
                        }
                        if (!taskBean.isReceive()) {
                            mWorkConsumablesB.setEnabled(true);
                            mWorkStart.setEnabled(false);
                        } else {
                            mWorkConsumablesB.setEnabled(false);
                            mWorkStart.setEnabled(true);
                        }
                        if (taskBean.getTaskStatus() == 0 ||
                                taskBean.getTaskStatus() == 1) {
                            mWorkStart.setEnabled(true);
                            mWorkStart.setText("开始任务");
                            taskStatus = 1;
                            mLocationClient.startLocation();
                        } else if (taskBean.getTaskStatus() == 2) {
                            mWorkStart.setEnabled(true);
                            mWorkStart.setText("结束任务");
                            mLocationClient.startLocation();
                            taskStatus = 2;
                        } else if (taskBean.getTaskStatus() == 3 ||
                                taskBean.getTaskStatus() == 4) {
                            mWorkStart.setEnabled(false);
                            mWorkStart.setText("结束任务");
                            mLocationB.setVisibility(View.GONE);
                            mLocationT.setVisibility(View.GONE);
                            mLocationI.setVisibility(View.GONE);
                            mWorkLocation.setVisibility(View.GONE);
                            taskStatus = 3;
                        }
                    }
                });

    }

    public void receiveConsumables(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("taskId", taskId);
        NetClient.getInstance(context).create(WorkService.class)
                .receiveConsumables("task/receiveConsumables",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                            mWorkConsumablesB.setEnabled(false);
                            mWorkStart.setEnabled(true);
                            taskStatus = 1;
                            ToastUtil.showToast(WorkDetailActivity.this, "领取成功");
                    }
                });
    }

    public void startTask(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("lng", "120.221424");//lng 120.221424
        map.put("lat", "30.210279");//lat 30.210279
        NetClient.getInstance(context).create(WorkService.class)
                .startTask("task/start",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        mWorkConsumablesB.setEnabled(false);
                        mWorkStart.setEnabled(true);
                        mWorkStart.setText("结束任务");
                        taskStatus = 2;
                    }
                });
    }

    public void completeTask(Context context) {
        ProgressDialogUtil.showProgressDialog(context);
        Map<String,String> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("lng", "120.221424");//lng 120.221424
        map.put("lat", "30.210279");//lat 30.210279
        NetClient.getInstance(context).create(WorkService.class)
                .completeTask("task/complete",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        mWorkConsumablesB.setEnabled(false);
                        mWorkStart.setEnabled(false);
                        taskStatus = 3;
                    }
                });
    }
}
