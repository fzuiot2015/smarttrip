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
import com.qin.pojo.breakalarmhistory.Result;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class BreakAlarmRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Result> mList;
    public Context mContext;

    public BreakAlarmRecyclerViewAdapter(List<Result> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_breakalarmhistory, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreakAlarmRecyclerViewAdapter.ViewHolder itemView = (BreakAlarmRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_breakalarm_des.setText("事故描述 : " + mList.get(position).getDes_casual());
        itemView.tv_breakalarm_address.setText("地址 ： " + mList.get(position).getAddress());
        itemView.tv_breakalarm_cardes.setText("汽车受损描述 : \n" + mList.get(position).getDes_car());
        String des_person = mList.get(position).getDes_person();
        if (des_person.equals("")){
            itemView.tv_breakalarm_persondes.setText("人员伤亡描述 : \n" + "无人员伤亡");
        }else{
            itemView.tv_breakalarm_persondes.setText("人员伤亡描述 : \n" + des_person);
        }
        itemView.tv_breakalarm_other.setText("其它描述 ： " + mList.get(position).getDes_others());
        itemView.tv_breakalarm_time.setText("时间 : " + mList.get(position).getTime() + ")");
        String pic_car = mList.get(position).getPic_car();
        String pic_person = mList.get(position).getPic_person();
        try {
            if (pic_car.equals("")) {
                Glide.with(mContext).load("").placeholder(R.mipmap.gas_blank).into(itemView.iv_breakalarm_car);
            } else {
                Glide.with(mContext).load(mList.get(position).getPic_car()).placeholder(R.mipmap.gas_blank).into(itemView.iv_breakalarm_car);
            }
            if (pic_person.equals("")) {
                Glide.with(mContext).load("").placeholder(R.mipmap.gas_blank).into(itemView.iv_breakalarm_person);
            } else {
                Glide.with(mContext).load(mList.get(position).getPic_person()).placeholder(R.mipmap.gas_blank).into(itemView.iv_breakalarm_person);
            }
        }catch (Exception e){
            e.printStackTrace();
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

        public ImageView iv_breakalarm_car;
        public ImageView iv_breakalarm_person;
        public TextView tv_breakalarm_address;
        public TextView tv_breakalarm_cardes;
        public TextView tv_breakalarm_persondes;
        public TextView tv_breakalarm_other;
        public TextView tv_breakalarm_time;
        public TextView tv_breakalarm_des;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_breakalarm_address = itemView.findViewById(R.id.tv_breakalarm_address);
            tv_breakalarm_cardes = itemView.findViewById(R.id.tv_breakalarm_cardes);
            tv_breakalarm_persondes = itemView.findViewById(R.id.tv_breakalarm_persondes);
            tv_breakalarm_other = itemView.findViewById(R.id.tv_breakalarm_other);
            tv_breakalarm_time = itemView.findViewById(R.id.tv_breakalarm_time);
            tv_breakalarm_des = itemView.findViewById(R.id.tv_breakalarm_des);
            iv_breakalarm_car = itemView.findViewById(R.id.iv_breakalarm_car);
            iv_breakalarm_person = itemView.findViewById(R.id.iv_breakalarm_person);
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
