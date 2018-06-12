package com.qin.adapter.recyclerview.gas;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qin.adapter.recyclerview.RecyclerViewItemBackground;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30 0013.
 */

public class NearbyAllGasRecycleAdapter extends RecyclerView.Adapter<NearbyAllGasRecycleAdapter.ViewHolder> implements View.OnClickListener {

    private List<String> mDataset;
    private AdapterView.OnItemClickListener mListener;

    public NearbyAllGasRecycleAdapter(List datas){
        this.mDataset = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), android.R.layout.simple_list_item_1,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder viewHolder = holder;
        viewHolder.mTextView.setText(mDataset.get(position));
        viewHolder.mTextView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        mOnItemClickListener.onItemClick(v,postion);
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            mTextView.setOnClickListener(NearbyAllGasRecycleAdapter.this);
            /**
             * 设置水波纹背景
             */
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }
}
