package com.qin.adapter.recyclerview.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.history.Result;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class ParkingHistoryRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Result> mList;

    public ParkingHistoryRecyclerViewAdapter(List<Result> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_drawer_historyfoot, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParkingHistoryRecyclerViewAdapter.ViewHolder itemView = (ParkingHistoryRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_address.setText(mList.get(position).getPlace());
        itemView.tv_content.setText(mList.get(position).getAccident());
        itemView.tv_cost.setText("停车费 ： "+mList.get(position).getCost()+"元");
        itemView.tv_time.setText(mList.get(position).getDate()+"  "+mList.get(position).getTime());
        itemView.itemView.setTag(position);
        itemView.mIv.setTag(position);
        itemView.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.iv:
                    mOnItemClickListener.onItemClick(v, ParkingHistoryRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
               default:
                    mOnItemClickListener.onItemClick(v, ParkingHistoryRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_address;
        public TextView tv_content;
        public TextView tv_time;
        public TextView tv_cost;
        public ImageView mIv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_time = itemView.findViewById(R.id.tv_time);
            mIv = itemView.findViewById(R.id.iv);
            itemView.setOnClickListener(ParkingHistoryRecyclerViewAdapter.this);
            mIv.setOnClickListener(ParkingHistoryRecyclerViewAdapter.this);
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public enum ViewName {
        TEXTVIEW,
        IMAGEVIEW,
        ITEM
    }

    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ViewName viewName, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
