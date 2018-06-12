package com.qin.adapter.recyclerview.usersearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.usersearch.repairshop.Contents;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class BreakAlarmRecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Contents> mList;

    public BreakAlarmRecyclerViewAdapter(List<Contents> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_userreapirshop, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BreakAlarmRecyclerViewAdapter.ViewHolder itemView = (BreakAlarmRecyclerViewAdapter.ViewHolder) holder;
        itemView.tv_repairshop_name.setText(mList.get(position).getTitle());
        itemView.tv_repairshop_address.setText(mList.get(position).getAddress());
        itemView.tv_repairshop_distance.setText("约"+mList.get(position).getDistance()+"米");
        itemView.tv_repairshop_score.setText("好评("+mList.get(position).getScore()+")");
        itemView.itemView.setTag(position);
        itemView.iv_repairshop_phone.setTag(position);
        itemView.iv_repairshop_navigation.setTag(position);
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
                case R.id.iv_repairshop_navigation:
                    mOnItemClickListener.onItemClick(v, BreakAlarmRecyclerViewAdapter.ViewName.IMAGEVIEW_NA, postion);
                    break;
                case R.id.iv_repairshop_phone:
                    mOnItemClickListener.onItemClick(v, BreakAlarmRecyclerViewAdapter.ViewName.IMAGEVIEW_PHONE, postion);
                    break;
//               default:
//                    mOnItemClickListener.onItemClick(v, RepairShopRecyclerViewAdapter.ViewName.ITEM, postion);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_repairshop_navigation;
        public ImageView iv_repairshop_photo;
        public ImageView iv_repairshop_phone;
        public TextView tv_repairshop_name;
        public TextView tv_repairshop_address;
        public TextView tv_repairshop_distance;
        public TextView tv_repairshop_score;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_repairshop_name = itemView.findViewById(R.id.tv_repairshop_name);
            tv_repairshop_address = itemView.findViewById(R.id.tv_repairshop_address);
            tv_repairshop_distance = itemView.findViewById(R.id.tv_repairshop_distance);
            tv_repairshop_score = itemView.findViewById(R.id.tv_repairshop_score);
            iv_repairshop_photo = itemView.findViewById(R.id.iv_repairshop_photo);
            iv_repairshop_navigation = itemView.findViewById(R.id.iv_repairshop_navigation);
            iv_repairshop_phone = itemView.findViewById(R.id.iv_repairshop_phone);

            itemView.setOnClickListener(BreakAlarmRecyclerViewAdapter.this);
            iv_repairshop_phone.setOnClickListener(BreakAlarmRecyclerViewAdapter.this);
            iv_repairshop_navigation.setOnClickListener(BreakAlarmRecyclerViewAdapter.this);
            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public enum ViewName {
        TEXTVIEW,
        IMAGEVIEW_NA,
        IMAGEVIEW_PHONE,
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
