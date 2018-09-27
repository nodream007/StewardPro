package com.nodream.xskj.module.main.inqusisition;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.nodream.xskj.commonlib.base.BaseFragment;
import com.nodream.xskj.commonlib.net.BaseObserver;
import com.nodream.xskj.commonlib.net.NetClient;
import com.nodream.xskj.commonlib.recyclerview.listener.OnScrollListener;
import com.nodream.xskj.commonlib.recyclerview.wrapper.LoadMoreWrapper;
import com.nodream.xskj.commonlib.view.SimpleToolbar;
import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.inqusisition.adapter.OrderCardAdapter;
import com.nodream.xskj.module.main.inqusisition.model.OrderBean;
import com.nodream.xskj.module.main.inqusisition.model.OrderService;
import com.nodream.xskj.module.main.work.adapter.WorkCardAdapter;
import com.nodream.xskj.module.main.work.model.TaskBean;
import com.nodream.xskj.module.main.work.model.WorkService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 问诊订单
 */
@Route(path = "/main/orderfragment")
public class OrderFragment extends BaseFragment implements OrderCardAdapter.OnItemClickListener {
    private View mView;
    private SimpleToolbar mSimpleToolbar;
    private TabLayout mTabLayout;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadMoreWrapper loadMoreWrapper;

    private String selectDay;

    private List<OrderBean> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_task, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSimpleToolbar = mView.findViewById(R.id.simple_toolbar);
        mTabLayout = mView.findViewById(R.id.fm_task_tl);
        mSimpleToolbar.setMainTitle("问诊");
        mSimpleToolbar.setRightImgDrawable(0, true);
        mSimpleToolbar.setRightImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/orderaddactivity").navigation();
            }
        });
        initTab();

        mRecyclerView = mView.findViewById(R.id.fm_task_rv);
        swipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
        // Set the refresh view color
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置adapter
        OrderCardAdapter orderCardAdapter = new OrderCardAdapter(products);
        orderCardAdapter.setOnItemClickListener(this);
        loadMoreWrapper = new LoadMoreWrapper(orderCardAdapter);
        //customLoadingView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(loadMoreWrapper);

        // Set the pull-down refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh data
//                products.clear();
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

    private void initTab() {
        TabLayout.Tab before_Yesterday = mTabLayout.newTab();
        before_Yesterday.setText("前天\n" + minusDay(2));
        before_Yesterday.setTag(minusDay(2));
        mTabLayout.addTab(before_Yesterday,0);

        TabLayout.Tab yesterday = mTabLayout.newTab();
        yesterday.setText("昨天\n" + minusDay(1));
        yesterday.setTag(minusDay(1));
        mTabLayout.addTab(yesterday,1);

        TabLayout.Tab todayTab = mTabLayout.newTab();
        todayTab.setText("今天\n" + nowDay());
        todayTab.setTag(nowDay());
        mTabLayout.addTab(todayTab,2,true);
        selectDay = nowDay();

        TabLayout.Tab tomorrow = mTabLayout.newTab();
        tomorrow.setText("明天\n" + addDay(1));
        tomorrow.setTag(addDay(1));
        mTabLayout.addTab(tomorrow,3);

        TabLayout.Tab after_tomorrow = mTabLayout.newTab();
        after_tomorrow.setText("后天\n" + addDay(2));
        after_tomorrow.setTag(addDay(2));
        mTabLayout.addTab(after_tomorrow,4);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectDay = tab.getTag().toString();
                getTaskList(getContext());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    public static String nowDay() {
        Calendar cal = Calendar.getInstance();
        String today = new SimpleDateFormat( "MM/dd").format(cal.getTime());
        return today;
    }
    private static String addDay(int n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        String addday = new SimpleDateFormat( "MM/dd").format(cal.getTime());
        return addday;
    }

    private static String minusDay(int n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -n);
        String minusday = new SimpleDateFormat( "MM/dd").format(cal.getTime());
        return minusday;
    }

    public void getTaskList(Context context) {
        Calendar cal = Calendar.getInstance();
        Map<String,String> map = new HashMap<>();
        int mYear = cal.get(Calendar.YEAR);
        String month = selectDay.substring(0,2);
        String day = selectDay.substring(3, 5);
        String searchDay = new StringBuffer().append(mYear).append("-").append(month)
                .append("-").append(day).toString();
        map.put("searchDay",searchDay);
        NetClient.getInstance(context).create(OrderService.class)
                .getOrderList("order/list",map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<OrderBean>>(context) {
                    @Override
                    protected void onSuccess(List<OrderBean> orderBeans) {
                        products.clear();
                        products.addAll(orderBeans);
                        loadMoreWrapper.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {
        ARouter.getInstance().build("/main/orderdetailactivity")
                .withString("orderId", products.get(position).getOrderId())
                .navigation();
    }
}
