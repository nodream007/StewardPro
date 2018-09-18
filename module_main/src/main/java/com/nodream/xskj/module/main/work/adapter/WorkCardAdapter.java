package com.nodream.xskj.module.main.work.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.work.model.TaskBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Pull up to load more.
 * <p>
 * Created by yangle on 2017/10/26.
 * Website：http://www.yangle.tech
 */

public class WorkCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaskBean> dataList;

    private OnItemClickListener onItemClickListener;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public WorkCardAdapter(List<TaskBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        recyclerViewHolder.tvWorkPatient.setText(dataList.get(position).getPatient().getName());
        String time = dataList.get(position).getPlanedStartTime().substring(10,16);
        recyclerViewHolder.tvWorkTime.setText(time);
        recyclerViewHolder.tvWorkName.setText(dataList.get(position).getProduct().getName());
        recyclerViewHolder.tvWorkAddress.setText(dataList.get(position).getServeLocation()
                .getAddress());
        String link = dataList.get(position).getLinkman() + " " + dataList.get(position)
                .getLinkMobile();
        recyclerViewHolder.tvWorkContact.setText(link);
        // Set item click listener
        if (onItemClickListener != null) {
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(recyclerViewHolder.itemView, recyclerViewHolder
                            .getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvWorkPatient;
        TextView tvWorkTime;
        TextView tvWorkName;
        TextView tvWorkAddress;
        TextView tvWorkContact;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvWorkPatient = itemView.findViewById(R.id.work_card_patient);
            tvWorkTime = itemView.findViewById(R.id.work_card_time);
            tvWorkName = itemView.findViewById(R.id.work_card_task);
            tvWorkAddress = itemView.findViewById(R.id.work_card_addr);
            tvWorkContact = itemView.findViewById(R.id.work_card_contact);
        }
    }

    /**
     * Set item click listener.
     *
     * @param onItemClickListener Click callback interface
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Click callback interface.
     */
    public interface OnItemClickListener {
        /**
         * Click callback method.
         *
         * @param view     Current view
         * @param position Click position
         */
        void onItemClick(View view, int position);
    }
}
