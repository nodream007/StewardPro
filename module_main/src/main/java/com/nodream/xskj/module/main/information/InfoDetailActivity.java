package com.nodream.xskj.module.main.information;

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

@Route(path = "/main/infodetailactivity")
public class InfoDetailActivity extends BaseActivity<AddWorkContract.View, AddWorkPresenter> {

    private SimpleToolbar mSimpleToolbar;

    private TextView mAddInfoNameX;
    private TextView mAddInfoName;
    private EditText mAddInfoMobile;
    private EditText mAddInfoIdentityCard;
    private TextView mAddInfoBirthday;
    private TextView mAddInfoGender;
    private EditText mAddInfoAddr;
    private EditText mAddInfoProfession;

    Calendar cal = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

    private String[] sexArry = new String[]{"男", "女"};// 性别选择

    @Autowired
    public int infoPid;


    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        ARouter.getInstance().inject(this);
        initView();
    }

    @Override
    protected AddWorkPresenter createPresenter() {
        return new AddWorkPresenter();
    }

    private void initView() {
        ToastUtil.showToast(this, infoPid + "-id");
        hideKey();
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("资料卡详情");
        mSimpleToolbar.setLeftTitleText("取消");
        mSimpleToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDetailActivity.this.finish();
            }
        });
        mSimpleToolbar.setRightTitleText("编辑");
        mSimpleToolbar.setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    checkParam();
                }
                isEdit = true;
                setTextAbled(true);
                showKey();
                mSimpleToolbar.setRightTitleText("完成");
            }
        });

        mAddInfoNameX= findViewById(R.id.info_detail_name_x);
        mAddInfoName = findViewById(R.id.info_detail_name);
        mAddInfoMobile = findViewById(R.id.info_add_mobile);
        mAddInfoIdentityCard = findViewById(R.id.info_add_identity_card);
        mAddInfoBirthday = findViewById(R.id.info_add_birthday);
        mAddInfoBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InfoDetailActivity.this, onDateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
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
        setTextAbled(false);

        getPatientDetail(this);
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
    private void updateDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mAddInfoBirthday.setText(simpleDateFormat.format(cal.getTime()));
    }
    private void checkParam() {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(mAddInfoName.getText().toString())) {
            mAddInfoName.setError("姓名不能为空");
            cancel = true;
            focusView = mAddInfoName;
        } else if (TextUtils.isEmpty(mAddInfoMobile.getText().toString())) {
            mAddInfoMobile.setError("电话不能为空");
            cancel = true;
            focusView = mAddInfoMobile;
        } else if (TextUtils.isEmpty(mAddInfoIdentityCard.getText().toString())) {
            mAddInfoIdentityCard.setError("身份证号不能为空");
            cancel = true;
            focusView = mAddInfoIdentityCard;
        } else if (TextUtils.isEmpty(mAddInfoAddr.getText().toString())) {
            mAddInfoAddr.setError("地址不能为空");
            cancel = true;
            focusView = mAddInfoAddr;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            submitPatient(this);
        }
    }
    private void getPatientDetail(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("patientId", Integer.toString(infoPid));
        NetClient.getInstance(context).create(InformationService.class)
                .getPatientDetail("patient/detail",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<PatientBean>(context) {
                    @Override
                    protected void onSuccess(PatientBean response) {
                        mAddInfoNameX.setText(response.getName().substring(0,1));
                        mAddInfoName.setText(response.getName());
                        mAddInfoMobile.setText(response.getMobile());
                        mAddInfoIdentityCard.setText(response.getIdentityCard());
                        mAddInfoBirthday.setText(response.getBirthday());
                        mAddInfoGender.setText(sexArry[response.getGender()]);
                        mAddInfoAddr.setText(response.getAddress());
                        mAddInfoProfession.setText(response.getProfession());
                    }
                });

    }

    private void setTextAbled(boolean enabled) {
        mAddInfoMobile.setEnabled(enabled);
        mAddInfoIdentityCard.setEnabled(enabled);
        mAddInfoBirthday.setEnabled(enabled);
        mAddInfoGender.setEnabled(enabled);
        mAddInfoAddr.setEnabled(enabled);
        mAddInfoProfession.setEnabled(enabled);
    }

    private void hideKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void showKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // 隐藏软键盘
            imm.showSoftInputFromInputMethod(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void submitPatient(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("id", Integer.toString(infoPid));
        map.put("name", mAddInfoName.getText().toString());
        map.put("mobile", mAddInfoMobile.getText().toString());
        map.put("identityCard", mAddInfoIdentityCard.getText().toString());
        map.put("birthday", mAddInfoBirthday.getText().toString());
        map.put("address", mAddInfoAddr.getText().toString());
        if (mAddInfoGender.getText().toString().equals("男")) {
            map.put("gender", "0");
        } else if (mAddInfoGender.getText().toString().equals("女")) {
            map.put("gender", "1");
        }
        map.put("profession", mAddInfoProfession.getText().toString());
        NetClient.getInstance(context).create(InformationService.class)
                .submitPatient("patient/edit",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(context) {
                    @Override
                    protected void onSuccess(String response) {
                        ToastUtil.showToast(InfoDetailActivity.this, "操作成功");
                        mSimpleToolbar.setRightTitleText("编辑");
                        isEdit = false;
                        hideKey();
                        setTextAbled(false);
                    }
                });

    }
}
