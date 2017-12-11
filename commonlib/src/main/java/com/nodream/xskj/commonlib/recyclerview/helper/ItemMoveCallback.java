package com.nodream.xskj.commonlib.recyclerview.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop.
 * <p>
 * Created by yangle on 2017/11/17.
 * Website：http://www.yangle.tech
 */

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private ItemMoveListener itemMoveListener;
    // true: item can move up|down|left|right
    // false: item can move up|down
    private boolean isFreedom;

    public ItemMoveCallback(ItemMoveListener itemMoveListener) {
        this.itemMoveListener = itemMoveListener;
    }

    public ItemMoveCallback(ItemMoveListener itemMoveListener, boolean isFreedom) {
        this.itemMoveListener = itemMoveListener;
        this.isFreedom = isFreedom;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager.
        int swipeFlags = 0;
        int dragFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            if (isFreedom) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move.
        itemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // NO OP
    }

    /**
     * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}.
     */
    public interface ItemMoveListener {

        /**
         * Called when an item has been dragged far enough to trigger a move.
         *
         * @param fromPosition The start position of the moved item.
         * @param toPosition   Then resolved position of the moved item.
         * @return True if the item was moved to the new adapter position.
         */
        boolean onItemMove(int fromPosition, int toPosition);
    }
}
