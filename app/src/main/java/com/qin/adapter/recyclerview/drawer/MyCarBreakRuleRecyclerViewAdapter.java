package com.qin.adapter.recyclerview.drawer;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.mycar.Illegal;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class MyCarBreakRuleRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Illegal> mList;

    public MyCarBreakRuleRecyclerViewAdapter(List<Illegal> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_drawer_mycarbr, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyCarBreakRuleRecyclerViewAdapter.ViewHolder itemView = (MyCarBreakRuleRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_address.setText("地址 ： " + mList.get(position).getProvince() + mList.get(position).getCity() + mList.get(position).getAddress());
        itemView.tv_content.setText("详情描述 ： " + mList.get(position).getContent());
        itemView.tv_time.setText(mList.get(position).getTime());
        itemView.tv_price.setText(mList.get(position).getPrice()+ " 元");
        String agency = mList.get(position).getAgency();
        if (TextUtils.isEmpty(agency)) {

        } else {
            itemView.tv_agency.setText("采集机关 ："+agency);
        }
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
                default:
                    mOnItemClickListener.onItemClick(v, MyCarBreakRuleRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_address;
        public TextView tv_content;
        public TextView tv_time;
        public TextView tv_price;
        public TextView tv_agency;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_agency = itemView.findViewById(R.id.tv_agency);
            tv_time = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(MyCarBreakRuleRecyclerViewAdapter.this);
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
