package com.nodream.xskj.module.main.my;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.utils.SharedPreferencesUtils;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.contact.model.MedicalStaffBean;
import com.nodream.xskj.module.main.information.model.InformationService;
import com.nodream.xskj.module.main.information.model.PatientBean;
import com.nodream.xskj.module.main.information.model.PinyinComparator;
import com.nodream.xskj.module.main.information.model.TitleItemDecoration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2017/12/4.
 */
@Route(path = "/main/myfragment")
public class MyFragment extends BaseFragment {

    private View mView;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private TextView myName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myName = mView.findViewById(R.id.my_name);
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());
        getPatientList(getContext());
    }

    private void getPatientList(Context context) {
        Map<String,String> map = new HashMap<>();
        int user_id = (int) sharedPreferencesUtils.getSharedPreference("user_id",0);
        map.put("medicalStaffId",Integer.toString(user_id));
        NetClient.getInstance(context).create(InformationService.class)
                .getContactDetail("medicalStaff/detail",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<MedicalStaffBean>(context) {
                    @Override
                    protected void onSuccess(MedicalStaffBean medicalStaffBean) {
                        myName.setText(medicalStaffBean.getName());
                    }
                });

    }
}
