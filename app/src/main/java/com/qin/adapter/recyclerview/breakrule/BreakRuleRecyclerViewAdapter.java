package com.qin.adapter.recyclerview.breakrule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.dao.LoginUsers;
import com.qin.pojo.breakrulequery.Lists;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class BreakRuleRecyclerViewAdapter extends RecyclerView.Adapter{

    private List<Lists> mDatas;
    private AdapterView.OnItemClickListener mListener;

    public BreakRuleRecyclerViewAdapter(List<Lists> datas) {
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_breakrule, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemholder = (ViewHolder) holder;
        itemholder.mTv_data.setText("违章时间 : "+mDatas.get(position).getDate());
        itemholder.mTv_area.setText("违章地点 : "+mDatas.get(position).getArea());
        itemholder.mTv_act.setText("违章行为 : "+mDatas.get(position).getAct());
        itemholder.mTv_code.setText("违章代码 : "+mDatas.get(position).getCode());
        itemholder.mTv_fen.setText("违章扣分 : "+mDatas.get(position).getFen());
        itemholder.mTv_money.setText("违章罚款 : "+mDatas.get(position).getMoney());
        itemholder.mTv_handled.setText("是否处理 : "+mDatas.get(position).getHandled());
        itemholder.mTv_payno.setText("交款编号 : "+mDatas.get(position).getPayNo());
        itemholder.mTv_longitude.setText("经度 : "+mDatas.get(position).getLongitude());
        itemholder.mTv_latitude.setText("纬度 : "+mDatas.get(position).getLatitude());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public  TextView mTv_data;
        public  TextView mTv_area;
        public  TextView mTv_act;
        public  TextView mTv_code;
        public  TextView mTv_fen;
        public  TextView mTv_money;
        public  TextView mTv_handled;
        public  TextView mTv_payno;
        public  TextView mTv_longitude;
        public  TextView mTv_latitude;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv_data = itemView.findViewById(R.id.tv_data);
            mTv_area = itemView.findViewById(R.id.tv_area);
            mTv_act = itemView.findViewById(R.id.tv_act);
            mTv_code = itemView.findViewById(R.id.tv_code);
            mTv_fen = itemView.findViewById(R.id.tv_fen);
            mTv_money = itemView.findViewById(R.id.tv_money);
            mTv_handled = itemView.findViewById(R.id.tv_handled);
            mTv_payno = itemView.findViewById(R.id.tv_payno);
            mTv_longitude = itemView.findViewById(R.id.tv_longitude);
            mTv_latitude = itemView.findViewById(R.id.tv_latitude);
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
}
