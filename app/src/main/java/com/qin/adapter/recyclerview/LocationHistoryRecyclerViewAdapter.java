package com.qin.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qin.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class LocationHistoryRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener{

    public List<String> mList;
    public OnItemClickListener mOnItemClickListener;

    public LocationHistoryRecyclerViewAdapter(List<String> list) {
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_location_history, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder view = (ViewHolder) holder;
        view.mText.setText(mList.get(position));
        view.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int)v.getTag();
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,postion);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.tv_content_his);
            itemView.setOnClickListener(LocationHistoryRecyclerViewAdapter.this);
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public void removeData(){
        mList.removeAll(mList);
        notifyDataSetChanged();
    }
}
