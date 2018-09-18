package com.nodream.xskj.module.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.work.WorkedFragment;
import com.nodream.xskj.module.main.work.WorkingFragment;

import java.util.ArrayList;

/**
 * Created by nodream on 2017/12/4.
 */
@Route(path = "/main/homefragment")
public class HomeFragment extends BaseFragment {
    private SimpleToolbar mSimpleToolbar;
    private TabLayout mTabLayout;

    ViewPager mViewPager;

    WorkingFragment workingFragment1;

    WorkedFragment workingFragment2;


    PagerAdapter mPagerAdapter;
    private ArrayList<String> titles = new ArrayList<>();

    private boolean isSelected = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSimpleToolbar = view.findViewById(R.id.simple_toolbar);
        mTabLayout = view.findViewById(R.id.home_tl);

        mSimpleToolbar.setMainTitle("任务");
        mSimpleToolbar.setRightImgDrawable(0, true);
        mSimpleToolbar.setRightImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/addworkactivity").navigation();
            }
        });

        mViewPager = view.findViewById(R.id.home_vp);
        mViewPager.setOffscreenPageLimit(2);
        workingFragment1 = new WorkingFragment();
        workingFragment2 = new WorkedFragment();
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText("进行中");
        mTabLayout.getTabAt(1).setText("已完成");
        mTabLayout.getTabAt(0).select();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Tab 进入选中状态时被调用
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            /**
             * Tab 离开选中状态时回调
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            /**
             * Tab 已经被选中又被选中时回调
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return workingFragment1;
            } else if (position == 1) {
                return workingFragment2;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
}
