package com.nodream.xskj.module.main.contact;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.contact.model.MedicalStaffBean;
import com.nodream.xskj.module.main.information.model.InformationService;
import com.nodream.xskj.module.main.information.model.PatientBean;
import com.nodream.xskj.module.main.work.AddWorkContract;
import com.nodream.xskj.module.main.work.presenter.AddWorkPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2018/08/21.
 */

@Route(path = "/main/contactdetailactivity")
public class ContactDetailActivity extends BaseActivity<AddWorkContract.View, AddWorkPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private TextView mContactNameX;
    private TextView mContactName;
    private TextView mContactMobile;
    private TextView mContactStation;

    @Autowired
    public int contactPid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ARouter.getInstance().inject(this);
        initView();
    }

    @Override
    protected AddWorkPresenter createPresenter() {
        return new AddWorkPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("通讯录");
        mSimpleToolbar.setLeftImgVisible();
        mSimpleToolbar.setLeftImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDetailActivity.this.finish();
            }
        });


        mContactNameX = findViewById(R.id.contact_detail_name_x);
        mContactName = findViewById(R.id.contact_detail_name);
        mContactMobile = findViewById(R.id.contact_mobile);
        mContactStation = findViewById(R.id.contact_station);

        getPatientDetail(this);
    }

    private void getPatientDetail(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("medicalStaffId", Integer.toString(contactPid));
        NetClient.getInstance(context).create(InformationService.class)
                .getContactDetail("medicalStaff/detail",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<MedicalStaffBean>(context) {
                    @Override
                    protected void onSuccess(MedicalStaffBean response) {
                        mContactNameX.setText(response.getName().substring(0,1));
                        mContactName.setText(response.getName());
                        mContactMobile.setText(response.getMobile());
                        mContactStation.setText(response.getPoint().getName());

                    }
                });

    }
}
