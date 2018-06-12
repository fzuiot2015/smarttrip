package com.qin.adapter.recyclerview.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class SearchOftenLocationAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<String> mDataset;
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;

    public SearchOftenLocationAdapter(Context context, List datas) {
        this.mDataset = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.listview_search_often, null);
        return new SearchOftenLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchOftenLocationAdapter.ViewHolder itemholder = (SearchOftenLocationAdapter.ViewHolder) holder;
        itemholder.mTv.setText(mDataset.get(position));
        itemholder.mTv.setTag(position);
        itemholder.mIv.setTag(position);
        itemholder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTv;
        private ImageView mIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv_search_often);
            mIv = itemView.findViewById(R.id.iv_search_often);
            mIv.setOnClickListener(SearchOftenLocationAdapter.this);
            mIv.setOnClickListener(SearchOftenLocationAdapter.this);
            itemView.setOnClickListener(SearchOftenLocationAdapter.this);
            /**
             * 设置水波纹背景
             */
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public enum ViewName {
        ITEM,
        TEXT,
        IMAGEVIEW
    }

    private SearchOftenLocationAdapter.OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SearchOftenLocationAdapter.ViewName name, int postion);
    }

    public void setOnRecyclerViewItemClickListener(SearchOftenLocationAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnRecyclerViewItemClickListener != null) {
            switch (v.getId()) {
                case R.id.iv_search_often:
                    mOnRecyclerViewItemClickListener.onItemClick(v, SearchOftenLocationAdapter.ViewName.IMAGEVIEW, postion);
                    break;
                case R.id.tv_search_often:
                    mOnRecyclerViewItemClickListener.onItemClick(v, SearchOftenLocationAdapter.ViewName.TEXT, postion);
                    break;
                default:
                    mOnRecyclerViewItemClickListener.onItemClick(v, SearchOftenLocationAdapter.ViewName.ITEM, postion);
                    break;
            }
        }
    }

    public void addData(int postion, String text) {
        mDataset.add(text);
        notifyDataSetChanged();
    }

    public void removeData(int postion) {
        mDataset.remove(postion);
        notifyDataSetChanged();
    }

    public void removeAllData() {
        mDataset.removeAll(mDataset);
        notifyDataSetChanged();
    }
}

