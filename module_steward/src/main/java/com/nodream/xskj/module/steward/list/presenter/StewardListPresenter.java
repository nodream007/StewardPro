package com.nodream.xskj.module.steward.list.presenter;

import com.nodream.xskj.commonlib.base.BasePresenter;
import com.nodream.xskj.module.steward.list.StewardListContract;
import com.nodream.xskj.module.steward.list.model.StewardListModel;

/**
 * Created by nodream on 2017/12/8.
 */

public class StewardListPresenter extends BasePresenter<StewardListContract.View> implements
        StewardListContract.Presenter {

    private final StewardListModel model;

    public StewardListPresenter() {
        this.model = new StewardListModel();
    }

}
