package com.qin.adapter.recyclerview.gas;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public abstract class AllGasRecycleAdapter extends RecyclerView.Adapter implements AdapterView.OnClickListener {

    public List<Data> mList;

    public AllGasRecycleAdapter(List<Data> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(parent.getContext(), R.layout.recyclerview_gas_all, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AllGasRecycleAdapter.ViewHolder itemView = (AllGasRecycleAdapter.ViewHolder) holder;
        onBbindViewHolder(itemView, position);

        itemView.itemView.setTag(position);
        itemView.mLl_gas_collection.setTag(position);
        itemView.mLl_gas_share.setTag(position);
    }

    protected abstract void onBbindViewHolder(AllGasRecycleAdapter.ViewHolder itemView, int position);

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
                    mOnItemClickListener.onItemClick(v, AllGasRecycleAdapter.ViewName.LL_GAS_COLLECTION, postion);
                    break;
                case R.id.ll_gas_share:
                    mOnItemClickListener.onItemClick(v, AllGasRecycleAdapter.ViewName.LL_GAS_SHARE, postion);
                    break;
                case R.id.ll_gas_tip:
                    mOnItemClickListener.onItemClick(v, AllGasRecycleAdapter.ViewName.LL_GAS_TIP, postion);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, AllGasRecycleAdapter.ViewName.ITEM, postion);
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
        public ImageView iv_gas_collection;
        LinearLayout mLl_gas_collection;
        LinearLayout mLl_gas_more;
        LinearLayout mLl_gas_share;

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

            iv_gas_collection = itemView.findViewById(R.id.iv_gas_collection);

            mLl_gas_collection = itemView.findViewById(R.id.ll_gas_collection);
            mLl_gas_more = itemView.findViewById(R.id.ll_gas_more);
            mLl_gas_share = itemView.findViewById(R.id.ll_gas_share);

            itemView.setOnClickListener(AllGasRecycleAdapter.this);
            mLl_gas_collection.setOnClickListener(AllGasRecycleAdapter.this);
            mLl_gas_share.setOnClickListener(AllGasRecycleAdapter.this);

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

    public AllGasRecycleAdapter refresh(List<Data> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }

    public AllGasRecycleAdapter loadMore(List<Data> list) {
        mList.addAll(list);
        notifyDataSetChanged();
        notifyListDataSetChanged();
        return this;
    }
    private final DataSetObservable mDataSetObservable = new DataSetObservable();


    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }
}
