package com.qin.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.dao.LoginUsers;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class LoginRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<LoginUsers> mDatas;
    private AdapterView.OnItemClickListener mListener;

    public LoginRecyclerViewAdapter(List datas) {
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_login_user, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemholder = (ViewHolder) holder;
        itemholder.mTv.setText(mDatas.get(position).getUser_phonenumber());
        itemholder.mIv.setTag(position);
        itemholder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIv;
        private TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.tv_username);
            mIv = itemView.findViewById(R.id.iv_delete);
            mIv.setOnClickListener(LoginRecyclerViewAdapter.this);
            itemView.setOnClickListener(LoginRecyclerViewAdapter.this);
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

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ViewName name, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.iv_delete:
                    mOnItemClickListener.onItemClick(v, ViewName.IMAGEVIEW, postion);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, postion);
                    break;
            }
        }
    }
}
