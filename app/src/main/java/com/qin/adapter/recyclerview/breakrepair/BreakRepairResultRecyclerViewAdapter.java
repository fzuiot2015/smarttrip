package com.qin.adapter.recyclerview.breakrepair;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.usersearch.repairshop.Contents;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class BreakRepairResultRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Contents> mList;

    public BreakRepairResultRecyclerViewAdapter(List<Contents> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_breakalarmresult, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreakRepairResultRecyclerViewAdapter.ViewHolder itemView = (BreakRepairResultRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_name.setText(mList.get(position).getTitle());
        itemView.tv_address.setText(mList.get(position).getAddress());
        itemView.tv_tel.setText(mList.get(position).getGarage_tel()+"");
        itemView.tv_distance.setText("距离 ： "+mList.get(position).getDistance()+"m");
        itemView.tv_tel.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, BreakRepairResultRecyclerViewAdapter.ViewName.TEXTVIEW, postion);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name;
        public TextView tv_address;
        public TextView tv_distance;
        public TextView tv_tel;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_tel = itemView.findViewById(R.id.tv_tel);
            tv_tel.setOnClickListener(BreakRepairResultRecyclerViewAdapter.this);
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
