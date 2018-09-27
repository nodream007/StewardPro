package com.nodream.xskj.module.main.information;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.utils.ToastUtil;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.contact.ClearEditText;
import com.nodream.xskj.module.main.contact.PinyinUtils;
import com.nodream.xskj.module.main.contact.WaveSideBar;
import com.nodream.xskj.module.main.information.adapter.InformationAdapter;
import com.nodream.xskj.module.main.information.model.InformationService;
import com.nodream.xskj.module.main.information.model.PatientBean;
import com.nodream.xskj.module.main.information.model.PinyinComparator;
import com.nodream.xskj.module.main.information.model.TitleItemDecoration;
import com.nodream.xskj.module.main.work.AddWorkContract;
import com.nodream.xskj.module.main.work.presenter.AddWorkPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

@Route(path = "/main/patientselectactivity")
public class PatientSelectActivity extends BaseActivity {

    private SimpleToolbar mSimpleToolbar;

    private RecyclerView mRecyclerView;
    private WaveSideBar mSideBar;
    private InformationAdapter mAdapter;
    private ClearEditText mClearEditText;

    private List<PatientBean> mPatientList = new ArrayList<>();
    private List<PatientBean> mDateList = new ArrayList<>();

    private LinearLayoutManager manager;
    private TitleItemDecoration mDecoration;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initView();
    }

    @Override
    protected AddWorkPresenter createPresenter() {
        return new AddWorkPresenter();
    }

    private void initView() {
        mSimpleToolbar = findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("选择病人");
        mSimpleToolbar.setLeftTitleText("取消");
        mSimpleToolbar.setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientSelectActivity.this.finish();
            }
        });

        mRecyclerView = findViewById(R.id.contact_rv);

        mSideBar = findViewById(R.id.sideBar);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        //RecyclerView设置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new InformationAdapter(this, mPatientList);
        mAdapter.setOnItemClickListener(new InformationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", mDateList.get(position).getId());
                intent.putExtra("name", mDateList.get(position).getName());
                PatientSelectActivity.this.setResult(0, intent);
                PatientSelectActivity.this.finish();
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mClearEditText = findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getPatientList(this);
    }
    private void getPatientList(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("keyword","");
        NetClient.getInstance(context).create(InformationService.class)
                .getPatientList("patient/list",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<PatientBean>>(context) {
                    @Override
                    protected void onSuccess(List<PatientBean> patientList) {
                        mPatientList = patientList;
                        mDateList = filledData(patientList);
                        mComparator = new PinyinComparator();
                        // 根据a-z进行排序源数据
                        Collections.sort(mDateList, mComparator);
                        mAdapter.updateList(mDateList);

                        mDecoration = new TitleItemDecoration(
                                PatientSelectActivity.this, mDateList);
                        //如果add两个，那么按照先后顺序，依次渲染。
                        mRecyclerView.addItemDecoration(mDecoration);
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                                PatientSelectActivity.this, DividerItemDecoration.VERTICAL));
                    }
                });

    }
    /**
     * 为RecyclerView填充数据
     *
     * @param patientList
     * @return
     */
    private List<PatientBean> filledData(List<PatientBean> patientList) {
        List<PatientBean> mSortList = new ArrayList<>();

        for (int i = 0; i < patientList.size(); i++) {
            String name = patientList.get(i).getName();
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                patientList.get(i).setLetters(sortString.toUpperCase());
            } else {
                patientList.get(i).setLetters("#");
            }

            mSortList.add(patientList.get(i));
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<PatientBean> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = filledData(mPatientList);
        } else {
            filterDateList.clear();
            for (PatientBean patientBean : mDateList) {
                String name = patientBean.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(patientBean);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mComparator);
        mDateList.clear();
        mDateList.addAll(filterDateList);
        mAdapter.notifyDataSetChanged();
    }

}
