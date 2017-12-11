package com.nodream.xskj.module.steward.list;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.nodream.xskj.commonlib.base.BaseActivity;
import com.nodream.xskj.commonlib.recyclerview.listener.OnScrollListener;
import com.nodream.xskj.commonlib.recyclerview.wrapper.LoadMoreWrapper;
import com.nodream.xskj.module.steward.R;
import com.nodream.xskj.module.steward.list.adapter.CommonAdapter;
import com.nodream.xskj.module.steward.list.presenter.StewardListPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nodream on 2017/12/8.
 */
@Route(path = "/steward/stewardlistactivity")
public class StewardListActivity extends BaseActivity<StewardListContract.View,
        StewardListPresenter> implements StewardListContract.View {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoadMoreWrapper loadMoreWrapper;
    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stewardlist);
        init();
    }

    @Override
    protected StewardListPresenter createPresenter() {
        return new StewardListPresenter();
    }

    @Override
    public void updateListData() {

    }

    private void init(){
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        // Set the refresh view color
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        CommonAdapter commonAdapter = new CommonAdapter(dataList);
        loadMoreWrapper = new LoadMoreWrapper(commonAdapter);
        //customLoadingView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(loadMoreWrapper);

        // Set the pull-down refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh data
                dataList.clear();
                getData();
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
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (dataList.size() < 52) {
                    // Simulate get network data，delay 1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getData();
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
    }

    /**
     * Simulate get data.
     */
    private void getData() {
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            dataList.add(String.valueOf(letter));
            letter++;
        }
    }
}
