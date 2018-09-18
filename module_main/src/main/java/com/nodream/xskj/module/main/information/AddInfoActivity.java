package com.nodream.xskj.module.main.information;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.information.model.InformationService;
import com.nodream.xskj.module.main.information.model.PatientBean;
import com.nodream.xskj.module.main.information.model.PinyinComparator;
import com.nodream.xskj.module.main.information.model.TitleItemDecoration;
import com.nodream.xskj.module.main.work.AddWorkContract;
import com.nodream.xskj.module.main.work.presenter.AddWorkPresenter;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2018/08/21.
 */

@Route(path = "/main/addinfoactivity")
public class AddInfoActivity extends BaseActivity<AddWorkContract.View, AddWorkPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private EditText mAddInfoName;
    private EditText mAddInfoMobile;
    private EditText mAddInfoIdentityCard;
    private TextView mAddInfoBirthday;
    private TextView mAddInfoGender;
    private EditText mAddInfoAddr;
    private EditText mAddInfoProfession;

    Calendar nowdate = Calendar.getInstance();
    int mYear = nowdate.get(Calendar.YEAR);
    int mMonth = nowdate.get(Calendar.MONTH);
    int mDay = nowdate.get(Calendar.DAY_OF_MONTH);
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            String days;
            days = new StringBuffer().append(mYear).append("-").append(mMonth).append("-").append(mDay).toString();
            mAddInfoBirthday.setText(days);
        }
    };

    private String[] sexArry = new String[]{"女", "男"};// 性别选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);
        initView();
    }

    @Override
    protected AddWorkPresenter createPresenter() {
        return new AddWorkPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("新建资料卡");
        mSimpleToolbar.setLeftTitleText("取消");
        mSimpleToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddInfoActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightTitleText("完成");
        mSimpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPatient(AddInfoActivity.this);
            }
        });

        mAddInfoName = findViewById(R.id.info_add_name);
        mAddInfoMobile = findViewById(R.id.info_add_mobile);
        mAddInfoIdentityCard = findViewById(R.id.info_add_identity_card);
        mAddInfoBirthday = findViewById(R.id.info_add_birthday);
        mAddInfoBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddInfoActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
            }
        });
        mAddInfoGender = findViewById(R.id.info_add_gender);
        mAddInfoGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexChooseDialog();
            }
        });
        mAddInfoAddr = findViewById(R.id.info_add_addr);
        mAddInfoProfession = findViewById(R.id.info_add_profession);
    }
    private void showSexChooseDialog() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
        builder3.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                mAddInfoGender.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder3.show();// 让弹出框显示
    }

    private void submitPatient(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("name",mAddInfoName.getText().toString());
        map.put("mobile",mAddInfoMobile.getText().toString());
        map.put("identityCard",mAddInfoIdentityCard.getText().toString());
        map.put("birthday",mAddInfoBirthday.getText().toString());
        map.put("address",mAddInfoAddr.getText().toString());
        if (mAddInfoGender.getText().toString().equals("男")) {
            map.put("gender","0");
        } else if (mAddInfoGender.getText().toString().equals("女")) {
            map.put("gender","1");
        }
        map.put("profession",mAddInfoProfession.getText().toString());
        NetClient.getInstance().create(InformationService.class)
                .submitPatient("patient.edit",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        ToastUtil.showToast(AddInfoActivity.this, "添加成功");
                        AddInfoActivity.this.finish();
                    }
                });

    }
}
