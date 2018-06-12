package com.qin.fragment.main.gas;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.gas.CityAllGasRecycleAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.gasstation.AllGasStation;
import com.qin.pojo.gasstation.Data;
import com.qin.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/3/30 0012.
 */

public class CityAllGasFragment extends BaseFragment{

    private static final String URL_CITY = "http://apis.haoservice.com/oil/region?key=0c3bcf8c01b04c3bbffceb48dd5859e9&city=";
    private static final String URL_PAGE = "&page=";
    private static final String URL_PAY = "&paybyvas=false";
    private static final String TAG = "AllGasFragment";
    private String city;
    private int page;
    private String mUrl;

    Unbinder unbinder;
    @BindView(R.id.recyclerView_shihuagas)
    RecyclerView recyclerViewShihuagas;
    private View mView;
    private List<Data> mDatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        mView = View.inflate(mActivity, R.layout.fragment_gas_cityall, null);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    protected void initData() {
        //正常加载显示的界面
        Log.i("viewpager", this.getClass().getName());
        accessNet(1);

    }

    private void accessNet(int page) {
        city = "福州";
        mUrl = URL_CITY + city + URL_PAGE + page + URL_PAY;
        OkGo.<String>post(mUrl).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i(TAG, response.body());
                parseData(response.body());
            }
        });
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
        List<Data> data = allGasStation.getResult().getData();
        Log.i(TAG, allGasStation.toString());
        mDatas.addAll(data);
        initDatas();
    }

    private void initDatas() {
        recyclerViewShihuagas.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShihuagas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewShihuagas.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        Log.i(TAG, "走到这" + mDatas.size());
        if (mDatas.size() != 0) {
            CityAllGasRecycleAdapter adapter = new CityAllGasRecycleAdapter(mDatas) {
                @Override
                protected void onBbindViewHolder(CityAllGasRecycleAdapter.ViewHolder itemView, int position) {
                    String name = mDatas.get(position).getName();
                    Log.i(TAG, name + "CityAllGasRecycleAdapter");
                    itemView.tv_gas_name.setText(name);
                    itemView.tv_gas_brandname.setText(mDatas.get(position).getBrandname());
                    itemView.tv_gas_discount.setText(mDatas.get(position).getDiscount());
                    itemView.tv_gas_exhaust.setText(mDatas.get(position).getExhaust());
                    itemView.tv_gas_location.setText(mDatas.get(position).getAddress());
                    itemView.tv_gas_e0.setText(mDatas.get(position).getPrice().getE0());
                    itemView.tv_gas_e90.setText(mDatas.get(position).getPrice().getE90());
                    itemView.tv_gas_e93.setText(mDatas.get(position).getPrice().getE93());
                    itemView.tv_gas_e97.setText(mDatas.get(position).getPrice().getE97());
                }
            };
            recyclerViewShihuagas.setAdapter(adapter);
            adapter.setOnItemClickListener(new CityAllGasRecycleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, CityAllGasRecycleAdapter.ViewName viewName, int postion) {
                    if (CityAllGasRecycleAdapter.ViewName.LL_GAS_COLLECTION == viewName) {
                        ToastUtils.showBgResource(mActivity, "收藏");
                    } else if (CityAllGasRecycleAdapter.ViewName.ITEM == viewName) {
                        ToastUtils.showBgResource(mActivity, "ITEM" + postion + "导航");
                    } else if (CityAllGasRecycleAdapter.ViewName.LL_GAS_SHARE == viewName) {
                        ToastUtils.showBgResource(mActivity, "分享");
                    } else if (CityAllGasRecycleAdapter.ViewName.LL_GAS_TIP == viewName) {
                        ToastUtils.showBgResource(mActivity, "提示");
                    }
                }
            });
        }
    }

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
