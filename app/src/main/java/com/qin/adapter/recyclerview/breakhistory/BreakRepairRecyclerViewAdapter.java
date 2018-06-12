package com.qin.adapter.recyclerview.breakhistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.breakrepairhistory.Result;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class BreakRepairRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Result> mList;
    public Context mContext;

    public BreakRepairRecyclerViewAdapter(List<Result> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_breakrepairhistory, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreakRepairRecyclerViewAdapter.ViewHolder itemView = (BreakRepairRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_breakrepair_des.setText("故障描述 : "+mList.get(position).getDescription());
        itemView.tv_breakrepair_address.setText(mList.get(position).getAddress());
        itemView.tv_breakrepair_model.setText("车型 ： "+mList.get(position).getModels());
        itemView.tv_breakrepair_other.setText("其它描述 ： "+mList.get(position).getOthers());
        itemView.tv_breakrepair_time.setText("时间 : "+mList.get(position).getTime()+")");
        String fault_photo = mList.get(position).getFault_photo();
        if (fault_photo.equals("")){
            Glide.with(mContext).load(R.mipmap.gas_blank).placeholder(R.mipmap.gas_blank).into(itemView.iv_breakrepair_photo);
        }else{
            Glide.with(mContext).load(mList.get(position).getFault_photo()).placeholder(R.mipmap.gas_blank).into(itemView.iv_breakrepair_photo);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_breakrepair_photo;
        public TextView tv_breakrepair_address;
        public TextView tv_breakrepair_des;
        public TextView tv_breakrepair_model;
        public TextView tv_breakrepair_other;
        public TextView tv_breakrepair_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_breakrepair_address = itemView.findViewById(R.id.tv_breakrepair_address);
            tv_breakrepair_des = itemView.findViewById(R.id.tv_breakrepair_des);
            tv_breakrepair_model = itemView.findViewById(R.id.tv_breakrepair_model);
            tv_breakrepair_other = itemView.findViewById(R.id.tv_breakrepair_other);
            tv_breakrepair_time = itemView.findViewById(R.id.tv_breakrepair_time);
            iv_breakrepair_photo = itemView.findViewById(R.id.iv_breakrepair_photo);
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public enum ViewName {
        TEXTVIEW,
        IMAGEVIEW_NA,
        IMAGEVIEW_PHONE,
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
