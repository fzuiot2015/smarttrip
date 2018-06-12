package com.qin.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qin.R;
import com.qin.pojo.carcontrol.Lists;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MainCarControlRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private ArrayList<Lists> mDatas;
    private Context context;

    public MainCarControlRecyclerViewAdapter(ArrayList<Lists> datas, Context context) {
        this.mDatas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_main_carcontrolmore, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemholder = (ViewHolder) holder;
        itemholder.title.setText(mDatas.get(position).getTitle());
        itemholder.content.setText(mDatas.get(position).getContent());
        itemholder.time.setText(mDatas.get(position).getTime());
        Glide.with(context).load(mDatas.get(position).getPic()).into(itemholder.iv);
        itemholder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView title;
        private TextView content;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_carcontrolmore_title);
            content = itemView.findViewById(R.id.tv_carcontrolmore_content);
            time = itemView.findViewById(R.id.tv_carcontrolmore_time);
            iv = itemView.findViewById(R.id.iv_carcontrol);
            itemView.setOnClickListener(MainCarControlRecyclerViewAdapter.this);
            /**
             * 设置水波纹背景
             */
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

   public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, position);
        }
    }
}
