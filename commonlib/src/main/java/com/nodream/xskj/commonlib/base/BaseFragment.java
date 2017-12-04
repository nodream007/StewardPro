package com.nodream.xskj.commonlib.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodream.xskj.commonlib.utils.Utils;

/**
 * Created by nodream on 2017/12/4.
 */

public class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    /**
     * 获取基类Activity
     * @return
     */
    protected BaseActivity getBaseActivity(){
        return mActivity;
    }
    protected void addFragment(BaseFragment fragment, @IdRes int frameId){
        Utils.checkNotNull(fragment);
        getBaseActivity().addFragment(fragment,frameId);
    }
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId){
        Utils.checkNotNull(fragment);
        getBaseActivity().replaceFragment(fragment,frameId);
    }
    protected void hideFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getBaseActivity().hideFragment(fragment);
    }
    protected void showFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getBaseActivity().showFragment(fragment);
    }
    protected void removeFragment(BaseFragment fragment){
        Utils.checkNotNull(fragment);
        getBaseActivity().removeFragment(fragment);
    }
    protected void popFragment(){
        getBaseActivity().popFragment();
    }
}
