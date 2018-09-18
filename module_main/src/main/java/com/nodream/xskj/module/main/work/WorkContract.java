package com.nodream.xskj.module.main.work;

import android.content.Context;

import com.nodream.xskj.commonlib.base.IBaseView;
import com.nodream.xskj.module.main.work.model.WorkRequest;

public class WorkContract {
    public interface View extends IBaseView {

    }
    public interface Presenter {
        void getTaskList();
    }
    public interface Modele {

    }
}
