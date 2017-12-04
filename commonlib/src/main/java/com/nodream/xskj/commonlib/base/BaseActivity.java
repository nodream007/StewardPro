package com.nodream.xskj.commonlib.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.utils.Utils;
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

    @Override
    public void showProgress(boolean show) {

    }

    /**
     * 添加Fragment
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    /**
     * 替换Fragment
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment,@IdRes int frameId){
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    /**
     * 隐藏Fragment
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();
    }

    /**
     * 显示Fragment
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();
    }

    /**
     * 移出Fragment
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
    }

    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
