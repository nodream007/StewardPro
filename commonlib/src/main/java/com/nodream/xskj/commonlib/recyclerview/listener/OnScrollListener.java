package com.nodream.xskj.commonlib.recyclerview.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView scroll listener.
 * <p>
 * Created by yangle on 2017/10/26.
 * Website：http://www.yangle.tech
 */

public abstract class OnScrollListener extends RecyclerView.OnScrollListener {

    // Used to mark whether scroll up.
    private boolean isScrollUp = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        // When not scroll
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Get the last fully displayed item position.
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            int itemCount = manager.getItemCount();

            // Scroll to the last item, and it's scroll up.
            if (lastItemPosition == (itemCount - 1) && isScrollUp) {
                // Load More
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // > 0 indicates that it is scroll up, <= 0 means stop or scroll down.
        isScrollUp = dy > 0;
    }

    /**
     * Callback method for scroll to end.
     */
    public abstract void onLoadMore();
}
