package com.nodream.xskj.module.main.work;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.recyclerview.listener.OnScrollListener;
import com.nodream.xskj.commonlib.recyclerview.wrapper.LoadMoreWrapper;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.work.adapter.WorkCardAdapter;
import com.nodream.xskj.module.main.work.model.TaskBean;
import com.nodream.xskj.module.main.work.model.WorkBean;
import com.nodream.xskj.module.main.work.model.WorkResponse;
import com.nodream.xskj.module.main.work.model.WorkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WorkedFragment extends BaseFragment implements WorkCardAdapter.OnItemClickListener {
    private View mView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadMoreWrapper loadMoreWrapper;

    private List<TaskBean> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_working, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = mView.findViewById(R.id.working_rv);
        swipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
        // Set the refresh view color
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置adapter
        WorkCardAdapter workCardAdapter = new WorkCardAdapter(products);
        workCardAdapter.setOnItemClickListener(this);
        loadMoreWrapper = new LoadMoreWrapper(workCardAdapter);
        //customLoadingView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(loadMoreWrapper);

        // Set the pull-down refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh data
                products.clear();
                getTaskList(getContext());
                loadMoreWrapper.notifyDataSetChanged();

                // Delay 1s close
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        // Set the load more listener
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (products.size() < 52) {
                    // Simulate get network data，delay 1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getTaskList(getContext());
                                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                } else {
                    // Show loading end
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });

        getTaskList(getContext());
    }

    public void getTaskList(Context context) {
//        Map<String,String> map = new HashMap<>();
//        map.put("taskStatus","3");
//        NetClient.getInstance().create(WorkService.class)
//                .getTaskList("task.list",map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<WorkResponse>(context) {
//                    @Override
//                    protected void onSuccess(WorkResponse workResponse) {
//                        products.clear();
//                        products.addAll(workResponse.getWorkBeanList());
//                        loadMoreWrapper.notifyDataSetChanged();
//                    }
//                });

    }

    @Override
    public void onItemClick(View view, int position) {
        ARouter.getInstance().build("/main/workdetailactivity").navigation();
    }
}
