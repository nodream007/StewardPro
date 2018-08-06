package com.nodream.xskj.module.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.base.BasePresenter;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.home.presenter.HomePresenter;
import com.nodream.xskj.module.main.inqusisition.InquisitionFragment;
import com.nodream.xskj.module.main.my.MyFragment;

import java.util.ArrayList;

/**
 * Created by nodream on 2017/12/1.
 */

@Route(path = "/main/homeactivity")
public class HomeActivity extends BaseActivity<HomeContract.View,HomePresenter> implements
        BottomNavigationView.OnNavigationItemSelectedListener{


    private ArrayList<BaseFragment> fragmentList;
    private HomeFragment homeFragment;
    private InquisitionFragment inquisitionFragment;
    private MyFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment = (HomeFragment) ARouter.getInstance()
                .build("/main/homefragment").navigation();
        fragment = (MyFragment) ARouter.getInstance()
                .build("/main/myfragment").navigation();
        inquisitionFragment = (InquisitionFragment) ARouter.getInstance()
                .build("/main/inquisitionfragment").navigation();
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_work);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);



    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.navigation_work) {
//            HomeFragment homeFragment = (HomeFragment) ARouter.getInstance()
//                    .build("/main/homefragment").navigation();
            selectItem(homeFragment);
        } else if (i == R.id.navigation_inquisition) {
            selectItem(inquisitionFragment);
        } else if (i == R.id.navigation_my) {
            if (isLogin()) {
//                MyFragment fragment = (MyFragment) ARouter.getInstance()
//                        .build("/main/myfragment").navigation();
                selectItem(fragment);
            } else {
                ARouter.getInstance().build("/login/singin").navigation();
            }

        } else {
            return false;
        }
        return true;
    }
    private void selectItem(BaseFragment fragment) {
        replaceFragment(fragment, R.id.content);
    }
    private boolean isLogin (){
        return true;
    }
}
