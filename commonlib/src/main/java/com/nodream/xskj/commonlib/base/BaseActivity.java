package com.nodream.xskj.commonlib.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by nodream on 2017/11/23.
 */

public class BaseActivity extends AppCompatActivity implements BaseView{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewManager.getInstance().finishActivity(this);
    }

//    @Override
//    public void setPresenter(Object presenter) {
//
//    }

    @Override
    public void showFailTip(String des) {
        ToastUtil.showToast(this,des);
    }
}
