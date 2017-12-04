package com.nodream.xskj.module.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.module.main.R;

/**
 * Created by nodream on 2017/12/4.
 */
@Route(path = "/main/homefragment")
public class HomeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}