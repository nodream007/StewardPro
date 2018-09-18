package com.nodream.xskj.module.main.location;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class GetLocation implements AMapLocationListener {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Context context;
    public Handler mHandler = null;

    public GetLocation(Context context,Handler mHandle) {
        this.context = context;
        this.mHandler = mHandle;
    }
    /*获取定位数据*/
    public void getLocation() {
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(this);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为低功耗模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        locationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        locationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();

    }
//    /*校验权限*/
//    public boolean CheckPermission (){
//        if (MyUtils.selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {
//            return  true;
//        }
//        else
//        { return  false;}
//    }

    /*j校验定位服务是否开启*/
    public boolean CheckAPSService(){
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                Message msg = new Message();
                msg.obj = amapLocation;
                msg.what = 1000;
                mHandler.sendMessage(msg);
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }

        locationClient.stopLocation();
    }

}
