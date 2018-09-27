package com.nodream.xskj.module.main.inqusisition.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nodream.xskj.module.main.R;
import com.nodream.xskj.module.main.inqusisition.model.OrderBean;

import java.util.List;

/**
 * Pull up to load more.
 * <p>
 * Created by yangle on 2017/10/26.
 * Website：http://www.yangle.tech
 */

public class OrderCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderBean> dataList;

    private OnItemClickListener onItemClickListener;


    public OrderCardAdapter(List<OrderBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
        recyclerViewHolder.tvOrderPatient.setText(dataList.get(position).getPatient().getName());
        int status = dataList.get(position).getOrderStatus();
        if (status == 0) {
            recyclerViewHolder.tvOrderStatus.setText("待指派");
        } else if (status == 1) {
            recyclerViewHolder.tvOrderStatus.setText("待问诊");
        } else if (status == 2) {
            recyclerViewHolder.tvOrderStatus.setText("待确认");
        } else if (status == 3) {
            recyclerViewHolder.tvOrderStatus.setText("已确认");
            recyclerViewHolder.tvOrderStatus.setTextColor(0xFF999999);
        } else if (status == 4) {
            recyclerViewHolder.tvOrderStatus.setText("已取消");
            recyclerViewHolder.tvOrderStatus.setTextColor(0xFFFF0000);
        } else if (status == 5) {
            recyclerViewHolder.tvOrderStatus.setText("已关闭");
            recyclerViewHolder.tvOrderStatus.setTextColor(0xFF999999);
        } else if (status == 6) {
            recyclerViewHolder.tvOrderStatus.setText("已完成");
        }
        String time = dataList.get(position).getInquiryTime().substring(10,16);
        recyclerViewHolder.tvOrderTime.setText(time);
        recyclerViewHolder.tvOrderContent.setText(dataList.get(position).getIllnessDesc());
        recyclerViewHolder.tvOrderAddress.setText(dataList.get(position).getServeLocation()
                .getAddress());
        String link = dataList.get(position).getLinkman() + " " + dataList.get(position)
                .getLinkMobile();
        recyclerViewHolder.tvOrderContact.setText(link);
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

        TextView tvOrderPatient;
        TextView tvOrderStatus;
        TextView tvOrderTime;
        TextView tvOrderContent;
        TextView tvOrderAddress;
        TextView tvOrderContact;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvOrderPatient = itemView.findViewById(R.id.order_card_patient);
            tvOrderStatus = itemView.findViewById(R.id.order_card_status);
            tvOrderTime = itemView.findViewById(R.id.order_card_time);
            tvOrderContent = itemView.findViewById(R.id.order_card_content);
            tvOrderAddress = itemView.findViewById(R.id.order_card_addr);
            tvOrderContact = itemView.findViewById(R.id.order_card_contact);
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
