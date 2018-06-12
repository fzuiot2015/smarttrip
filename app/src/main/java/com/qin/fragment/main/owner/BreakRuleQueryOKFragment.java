package com.qin.fragment.main.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qin.R;
import com.qin.adapter.recyclerview.breakrule.BreakRuleRecyclerViewAdapter;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.breakrulequery.Lists;
import com.qin.pojo.breakrulequery.Result;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakRuleQueryOKFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView_breakrule)
    RecyclerView recyclerViewBreakrule;
    Unbinder unbinder;
    @BindView(R.id.tv_nobreakrule)
    TextView tvNobreakrule;
    @BindView(R.id.tv_provincecode)
    TextView tvProvincecode;
    @BindView(R.id.tv_citycode)
    TextView tvCitycode;
    @BindView(R.id.tv_hphm)
    TextView tvHphm;
    @BindView(R.id.btn_againcheck)
    Button btnAgaincheck;
    private Result mResult;
    private ArrayList<Lists> mList;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Lists> mDatas;
    private BreakRuleRecyclerViewAdapter mAdapter;

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_breakrule_query_success, null, false);
        mList = getArguments().getParcelableArrayList("list");
        mResult = getArguments().getParcelable("result");
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

        if (mList != null && mResult != null || mResult.getLists().size() > 0) {
            Log.i("list", mList.size() + "---" + mResult.getHphm());
            mDatas = mResult.getLists();
            recyclerViewBreakrule.setVisibility(View.VISIBLE);
            tvNobreakrule.setVisibility(View.GONE);
            tvCitycode.setText(mResult.getCity());
            tvProvincecode.setText(mResult.getProvince());
            tvHphm.setText(mResult.getHphm());
            initRecyclerView();
        } else {
            Log.i("list", "null");
            recyclerViewBreakrule.setVisibility(View.GONE);
            tvNobreakrule.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerViewBreakrule.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBreakrule.setLayoutManager(mLayoutManager);
        recyclerViewBreakrule.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mAdapter = new BreakRuleRecyclerViewAdapter(mDatas);
        recyclerViewBreakrule.setAdapter(mAdapter);
        recyclerViewBreakrule.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_againcheck)
    public void onViewClicked() {
getFragmentManager().beginTransaction().replace(R.id.fl_owner,new BreakRuleQueryFragment()).commit();
    }
}
