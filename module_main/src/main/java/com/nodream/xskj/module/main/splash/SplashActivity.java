package com.nodream.xskj.module.main.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.base.BasePresenter;
import com.nodream.xskj.module.main.home.HomeActivity;
import com.nodream.xskj.module.main.splash.presenter.SplashPresenter;

/**
 * Created by nodream on 2017/11/24.
 */

public class SplashActivity extends BaseActivity<SplashContract.View, SplashPresenter> implements
        SplashContract.View {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().build("/main/homeactivity").navigation();
//        ARouter.getInstance().build("/login/loginactivity").navigation();
//        startActivity(new Intent(this,HomeActivity.class));
        this.finish();
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter();
    }
}
