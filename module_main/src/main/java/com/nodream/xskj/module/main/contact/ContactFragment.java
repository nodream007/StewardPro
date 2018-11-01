package com.nodream.xskj.module.main.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.contact.adapter.ContactAdapter;
import com.nodream.xskj.module.main.contact.model.MedicalStaffBean;
import com.nodream.xskj.module.main.information.model.InformationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nodream on 2017/12/4.
 */
@Route(path = "/main/contactfragment")
public class ContactFragment extends BaseFragment {
    private View mView;
    private SimpleToolbar mSimpleToolbar;
    private RecyclerView mRecyclerView;
    private WaveSideBar mSideBar;
    private ContactAdapter mAdapter;
    private ClearEditText mClearEditText;

    private LinearLayoutManager manager;
    private List<MedicalStaffBean> mDateList = new ArrayList<>();

    private TitleItemDecoration mDecoration;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator mComparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(mView);
    }



    private void initView(View view) {
        mSimpleToolbar = view.findViewById(R.id.simple_toolbar);
        mSimpleToolbar.setMainTitle("通讯录");

        mRecyclerView = mView.findViewById(R.id.contact_rv);

        mSideBar = mView.findViewById(R.id.sideBar);
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
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new ContactAdapter(getContext(), mDateList);
        mAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ARouter.getInstance().build("/main/contactdetailactivity")
                        .withInt("contactPid", mDateList.get(position).getId())
                        .navigation();
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mClearEditText = mView.findViewById(R.id.filter_edit);

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

        getPatientList(getContext());

    }

    private void getPatientList(Context context) {
        Map<String,String> map = new HashMap<>();
        map.put("keyword","");
        NetClient.getInstance(context).create(InformationService.class)
                .getContactList("medicalStaff/list",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<MedicalStaffBean>>(context) {
                    @Override
                    protected void onSuccess(List<MedicalStaffBean> medicalStaffBeans) {
//                        mPatientList = patientList;
                        mDateList = filledData(medicalStaffBeans);
                        mComparator = new PinyinComparator();
                        // 根据a-z进行排序源数据
                        Collections.sort(mDateList, mComparator);
                        mAdapter.updateList(mDateList);

                        mDecoration = new TitleItemDecoration(getContext(), mDateList);
                        //如果add两个，那么按照先后顺序，依次渲染。
                        mRecyclerView.addItemDecoration(mDecoration);
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    }
                });

    }
    /**
     * 为RecyclerView填充数据
     *
     * @param mDateList
     * @return
     */
    private List<MedicalStaffBean> filledData(List<MedicalStaffBean> mDateList) {
        List<MedicalStaffBean> mSortList = new ArrayList<>();

        for (int i = 0; i < mDateList.size(); i++) {
            String name = mDateList.get(i).getName();
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                mDateList.get(i).setLetters(sortString.toUpperCase());
            } else {
                mDateList.get(i).setLetters("#");
            }

            mSortList.add(mDateList.get(i));
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<MedicalStaffBean> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = filledData(mDateList);
        } else {
            filterDateList.clear();
            for (MedicalStaffBean medicalStaffBean : mDateList) {
                String name = medicalStaffBean.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(medicalStaffBean);
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
