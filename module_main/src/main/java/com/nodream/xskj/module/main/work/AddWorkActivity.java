package com.nodream.xskj.module.main.work;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.work.presenter.AddWorkPresenter;
import com.nodream.xskj.module.main.R;

/**
 * Created by nodream on 2018/08/21.
 */

@Route(path = "/main/addworkactivity")
public class AddWorkActivity extends BaseActivity<AddWorkContract.View, AddWorkPresenter> {

    private SimpleToolbar mSimpleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_add);
        initView();
    }

    @Override
    protected AddWorkPresenter createPresenter() {
        return new AddWorkPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("新建任务");
        mSimpleToolbar.setLeftTitleText("取消");
        mSimpleToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWorkActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightTitleText("发布");
        mSimpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(AddWorkActivity.this, "发布");
            }
        });
    }

    protected void submit() {

    }
}
