package com.qin.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class LocationNearbyPoiRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<String> mList;

    public LocationNearbyPoiRecyclerViewAdapter(List<String> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_location_nearbypoi, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LocationNearbyPoiRecyclerViewAdapter.ViewHolder itemView = (LocationNearbyPoiRecyclerViewAdapter.ViewHolder) holder;
        itemView.mTv.setText(mList.get(position));
        itemView.mTv.setTag(position);
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
                case R.id.iv_navigation:
                    mOnItemClickListener.onItemClick(v, LocationNearbyPoiRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, LocationNearbyPoiRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTv;
        public ImageView mIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv_content);
            mIv = itemView.findViewById(R.id.iv_navigation);
            itemView.setOnClickListener(LocationNearbyPoiRecyclerViewAdapter.this);
            mIv.setOnClickListener(LocationNearbyPoiRecyclerViewAdapter.this);
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
