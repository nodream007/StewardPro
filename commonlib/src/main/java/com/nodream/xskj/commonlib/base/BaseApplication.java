package com.nodream.xskj.commonlib.base;

import android.app.Application;

import com.nodream.xskj.commonlib.utils.Utils;


/**
 * Created by nodream on 2017/11/24.
 */

public class BaseApplication extends Application {

    private  static BaseApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Utils.init(this);
    }
}
