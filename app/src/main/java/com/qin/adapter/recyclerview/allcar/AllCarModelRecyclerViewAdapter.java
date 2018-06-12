package com.qin.adapter.recyclerview.allcar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.allcartype.carmodel.Lists;
import com.qin.pojo.allcartype.carmodel.Result;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class AllCarModelRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Lists> mDatas;
    private AdapterView.OnItemClickListener mListener;
    private Context context;

    public AllCarModelRecyclerViewAdapter(List<Lists> datas, Context context) {
        this.mDatas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_carmodel_all, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemholder = (ViewHolder) holder;
        itemholder.name.setText(mDatas.get(position).getFullname());
        itemholder.salestate.setText(mDatas.get(position).getSalestate());
        Glide.with(context).load(mDatas.get(position).getLogo()).into(itemholder.iv);
        itemholder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView name;
        private TextView salestate;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_carmodel_name);
            salestate = itemView.findViewById(R.id.tv_carmodel_salestate);
            iv = itemView.findViewById(R.id.iv_carmodel);
            itemView.setOnClickListener(AllCarModelRecyclerViewAdapter.this);
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
