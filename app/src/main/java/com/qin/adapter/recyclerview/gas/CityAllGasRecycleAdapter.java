package com.qin.adapter.recyclerview.gas;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.RecyclerViewItemBackground;
import com.qin.pojo.gasstation.Data;

import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public abstract class CityAllGasRecycleAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Data> mList;

    public CityAllGasRecycleAdapter(List<Data> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_gas_cityall, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CityAllGasRecycleAdapter.ViewHolder itemView = (CityAllGasRecycleAdapter.ViewHolder) holder;
        onBbindViewHolder(itemView, position);

        itemView.itemView.setTag(position);
        itemView.mLl_gas_collection.setTag(position);
        itemView.mLl_gas_share.setTag(position);
        itemView.mLl_gas_tip.setTag(position);
    }

    protected abstract void onBbindViewHolder(CityAllGasRecycleAdapter.ViewHolder itemView, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int postion = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.ll_gas_collection:
                    mOnItemClickListener.onItemClick(v, CityAllGasRecycleAdapter.ViewName.LL_GAS_COLLECTION, postion);
                    break;
                case R.id.ll_gas_share:
                    mOnItemClickListener.onItemClick(v, CityAllGasRecycleAdapter.ViewName.LL_GAS_SHARE, postion);
                    break;
                case R.id.ll_gas_tip:
                    mOnItemClickListener.onItemClick(v, CityAllGasRecycleAdapter.ViewName.LL_GAS_TIP, postion);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, CityAllGasRecycleAdapter.ViewName.ITEM, postion);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        boolean isShow = false;
        TextView mTv_gas_more;
        public TextView tv_gas_name;
        public TextView tv_gas_location;
        public TextView tv_gas_discount;
        public TextView tv_gas_brandname;
        public TextView tv_gas_exhaust;
        public TextView tv_gas_e0;
        public TextView tv_gas_e90;
        public TextView tv_gas_e93;
        public TextView tv_gas_e97;
        LinearLayout mLl_gas_collection;
        LinearLayout mLl_gas_more;
        LinearLayout mLl_gas_share;
        LinearLayout mLl_gas_tip;

        public ViewHolder(View itemView) {
            super(itemView);

            mTv_gas_more = itemView.findViewById(R.id.tv_gas_more);
            tv_gas_name = itemView.findViewById(R.id.tv_gas_name);
            tv_gas_location = itemView.findViewById(R.id.tv_gas_location);
            tv_gas_discount = itemView.findViewById(R.id.tv_gas_discount);
            tv_gas_brandname = itemView.findViewById(R.id.tv_gas_brandname);
            tv_gas_exhaust = itemView.findViewById(R.id.tv_gas_exhaust);
            tv_gas_e0 = itemView.findViewById(R.id.tv_gas_e0);
            tv_gas_e90 = itemView.findViewById(R.id.tv_gas_e90);
            tv_gas_e93 = itemView.findViewById(R.id.tv_gas_e93);
            tv_gas_e97 = itemView.findViewById(R.id.tv_gas_e97);
            mLl_gas_collection = itemView.findViewById(R.id.ll_gas_collection);
            mLl_gas_more = itemView.findViewById(R.id.ll_gas_more);
            mLl_gas_share = itemView.findViewById(R.id.ll_gas_share);
            mLl_gas_tip = itemView.findViewById(R.id.ll_gas_tip);

            itemView.setOnClickListener(CityAllGasRecycleAdapter.this);
            mLl_gas_collection.setOnClickListener(CityAllGasRecycleAdapter.this);
            mLl_gas_share.setOnClickListener(CityAllGasRecycleAdapter.this);
            mLl_gas_tip.setOnClickListener(CityAllGasRecycleAdapter.this);

            mTv_gas_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isShow) {
                        mLl_gas_more.setVisibility(View.VISIBLE);
                        isShow = !isShow;
                    } else {
                        mLl_gas_more.setVisibility(View.GONE);
                        isShow = !isShow;
                    }
                }
            });

            if (itemView.getBackground() == null) {
                new RecyclerViewItemBackground(itemView).setRecyclerViewItemBackground();
            }
        }
    }

    public enum ViewName {
        TEXTVIEW,
        TV_GAS_MORE,
        LL_GAS_COLLECTION,
        LL_GAS_SHARE,
        LL_GAS_TIP,
        ITEM
    }

    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ViewName viewName, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void addData(int postion, Data data) {
        mList.add(data);
        notifyDataSetChanged();
    }
}
