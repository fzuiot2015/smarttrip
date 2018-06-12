package com.qin.adapter.recyclerview.collect;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.usercollect.Result;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class CollectRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Result> mList;

    public CollectRecyclerViewAdapter(List<Result> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_usercollect, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectRecyclerViewAdapter.ViewHolder itemView = (CollectRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_collect_name.setText(mList.get(position).getName());
        itemView.tv_collect_address.setText(mList.get(position).getAddress());
        itemView.itemView.setTag(position);
        itemView.iv_collect_delete.setTag(position);
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
                case R.id.iv_collect_delete:
                    mOnItemClickListener.onItemClick(v, CollectRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
               default:
                    mOnItemClickListener.onItemClick(v, CollectRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_collect_delete;
        public TextView tv_collect_name;
        public TextView tv_collect_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_collect_name = itemView.findViewById(R.id.tv_collect_name);
            tv_collect_address = itemView.findViewById(R.id.tv_collect_address);
            iv_collect_delete = itemView.findViewById(R.id.iv_collect_delete);
            itemView.setOnClickListener(CollectRecyclerViewAdapter.this);
            iv_collect_delete.setOnClickListener(CollectRecyclerViewAdapter.this);
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
