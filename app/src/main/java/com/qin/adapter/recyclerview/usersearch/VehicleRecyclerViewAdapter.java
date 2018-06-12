package com.qin.adapter.recyclerview.usersearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.usersearch.vihecle.Results;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Results> mList;

    public VehicleRecyclerViewAdapter(List<Results> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_userwashcar, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VehicleRecyclerViewAdapter.ViewHolder itemView = (VehicleRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_parking_nam.setText(mList.get(position).getName());
        itemView.tv_parking_address.setText(mList.get(position).getAddress());
        itemView.itemView.setTag(position);
        itemView.iv_parking_navigation.setTag(position);
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
                case R.id.iv_parking_navigation:
                    mOnItemClickListener.onItemClick(v, VehicleRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
               default:
                    mOnItemClickListener.onItemClick(v, VehicleRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_parking_navigation;
        public ImageView iv_parking_photo;
        public TextView tv_parking_nam;
        public TextView tv_parking_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_parking_nam = itemView.findViewById(R.id.tv_parking_nam);
            tv_parking_address = itemView.findViewById(R.id.tv_parking_address);
            iv_parking_navigation = itemView.findViewById(R.id.iv_parking_navigation);
            iv_parking_photo = itemView.findViewById(R.id.iv_parking_photo);
            itemView.setOnClickListener(VehicleRecyclerViewAdapter.this);
            iv_parking_navigation.setOnClickListener(VehicleRecyclerViewAdapter.this);
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
