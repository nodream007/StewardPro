package com.nodream.xskj.module.steward.list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.base.BasePresenter;
import com.nodream.xskj.module.steward.list.presenter.StewardListPresenter;

/**
 * Created by nodream on 2017/12/8.
 */
@Route(path = "/steward/stewardlistactivity")
public class StewardListActivity extends BaseActivity<StewardListContract.View,
        StewardListPresenter> implements StewardListContract.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected StewardListPresenter createPresenter() {
        return new StewardListPresenter();
    }

    @Override
    public void updateListData() {

    }
}
