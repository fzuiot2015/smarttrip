package com.qin.adapter.recyclerview.breakrule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.breakrule.Result;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class BreakRuleHappenRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Result> mList;

    public BreakRuleHappenRecyclerViewAdapter(List<Result> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_breakrule_happen, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreakRuleHappenRecyclerViewAdapter.ViewHolder itemView = (BreakRuleHappenRecyclerViewAdapter.ViewHolder) holder;
        String distance = mList.get(position).getDistance();
        int d = (int) Double.parseDouble(distance);
        itemView.tv_breakrulehappen_address.setText(mList.get(position).getAddress()+"距离您大约"+d+"米");
        itemView.tv_breakrulehappen_content.setText(mList.get(position).getContent());
        itemView.tv_breakrulehappen_num.setText(mList.get(position).getNum());
        itemView.itemView.setTag(position);
        itemView.ll_breakrulehappen_mark.setTag(position);
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
                case R.id.ll_breakrulehappen_mark:
                    mOnItemClickListener.onItemClick(v, BreakRuleHappenRecyclerViewAdapter.ViewName.IMAGEVIEW, postion);
                    break;
               default:
                    mOnItemClickListener.onItemClick(v, BreakRuleHappenRecyclerViewAdapter.ViewName.ITEM, postion);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public  LinearLayout ll_breakrulehappen_mark;
        public ImageView iv_breakrulehappen_mark;
        public ImageView iv_breakrulehappen_photo;
        public TextView tv_breakrulehappen_content;
        public TextView tv_breakrulehappen_address;
        public TextView tv_breakrulehappen_num;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_breakrulehappen_content = itemView.findViewById(R.id.tv_breakrulehappen_content);
            tv_breakrulehappen_address = itemView.findViewById(R.id.tv_breakrulehappen_address);
            ll_breakrulehappen_mark = itemView.findViewById(R.id.ll_breakrulehappen_mark);
            tv_breakrulehappen_num = itemView.findViewById(R.id.tv_breakrulehappen_num);
            iv_breakrulehappen_mark = itemView.findViewById(R.id.iv_breakrulehappen_mark);
            iv_breakrulehappen_photo = itemView.findViewById(R.id.iv_breakrulehappen_photo);
            itemView.setOnClickListener(BreakRuleHappenRecyclerViewAdapter.this);
            ll_breakrulehappen_mark.setOnClickListener(BreakRuleHappenRecyclerViewAdapter.this);
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
