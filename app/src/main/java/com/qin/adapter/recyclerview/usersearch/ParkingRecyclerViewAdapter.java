package com.qin.adapter.recyclerview.usersearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.usersearch.parking.Contents;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class ParkingRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Contents> mList;

    public ParkingRecyclerViewAdapter(List<Contents> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_userparking, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParkingRecyclerViewAdapter.ViewHolder itemView = (ParkingRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_parking_nam.setText(mList.get(position).getTitle());
        itemView.tv_parking_address.setText(mList.get(position).getAddress());
        itemView.tv_parking_distance.setText(mList.get(position).getDistance()+"米");
        itemView.tv_parking_emptyseat.setText(
                "剩余车位 : "+mList.get(position).getParking_rest()+"/"
                        +mList.get(position).getParking_all());
        itemView.tv_parking_price.setText("价格 : "+mList.get(position).getParking_charge()+"元/小时");
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
                    mOnItemClickListener.onItemClick(v, ParkingRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
               default:
                    mOnItemClickListener.onItemClick(v, ParkingRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_parking_navigation;
        public ImageView iv_parking_photo;
        public TextView tv_parking_nam;
        public TextView tv_parking_price;
        public TextView tv_parking_emptyseat;
        public TextView tv_parking_address;
        public TextView tv_parking_distance;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_parking_nam = itemView.findViewById(R.id.tv_parking_nam);
            tv_parking_address = itemView.findViewById(R.id.tv_parking_address);
            tv_parking_price = itemView.findViewById(R.id.tv_parking_price);
            tv_parking_emptyseat = itemView.findViewById(R.id.tv_parking_emptyseat);
            tv_parking_distance = itemView.findViewById(R.id.tv_parking_distance);
            iv_parking_navigation = itemView.findViewById(R.id.iv_parking_navigation);
            iv_parking_photo = itemView.findViewById(R.id.iv_parking_photo);
            itemView.setOnClickListener(ParkingRecyclerViewAdapter.this);
            iv_parking_navigation.setOnClickListener(ParkingRecyclerViewAdapter.this);
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
