package com.qin.adapter.recyclerview.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class SearchHistoryLocationAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<String> mDataset;
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;

    public SearchHistoryLocationAdapter(Context context, List datas) {
        this.mDataset = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.listview_serachistoryloc, null);
        return new SearchHistoryLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHistoryLocationAdapter.ViewHolder itemholder = (SearchHistoryLocationAdapter.ViewHolder) holder;
        itemholder.mTv.setText(mDataset.get(position));
        itemholder.mItem.setTag(position);
        itemholder.mIv.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mDataset.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mItem;
        private TextView mTv;
        private ImageView mIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem = itemView.findViewById(R.id.rl_lv_location);
            mTv = itemView.findViewById(R.id.tv_lv_location);
            mIv = itemView.findViewById(R.id.iv);
            mItem.setOnClickListener(SearchHistoryLocationAdapter.this);
            mIv.setOnClickListener(SearchHistoryLocationAdapter.this);
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

    private SearchHistoryLocationAdapter.OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, SearchHistoryLocationAdapter.ViewName name, int postion);
    }

    public void setOnRecyclerViewItemClickListener(SearchHistoryLocationAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnRecyclerViewItemClickListener != null) {
            switch (v.getId()) {
                case R.id.iv:
                    mOnRecyclerViewItemClickListener.onItemClick(v, SearchHistoryLocationAdapter.ViewName.IMAGEVIEW, postion);
                    break;
                case R.id.rl_lv_location:
                    mOnRecyclerViewItemClickListener.onItemClick(v, SearchHistoryLocationAdapter.ViewName.ITEM, postion);
                    break;
            }
        }
    }

    public void addData(int postion, String text) {
        mDataset.add(postion, text);
        notifyItemInserted(postion);
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

