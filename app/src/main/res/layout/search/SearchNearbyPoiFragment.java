package com.qin.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.R;
import com.qin.adapter.recyclerview.SearchNearbyPoiRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;


/**
 * Created by Administrator on 2018/3/17 0017.
 */

public class SearchNearbyPoiFragment extends Fragment {
    @InjectView(R.id.search_rlv_nearbypoi)
    RecyclerView searchRlvNearbypoi;
    private Activity mActivity;
    private View mView;

    private ArrayList<String> mLists;
    private SearchNearbyPoiRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_search_fragment_nearbypoi, null, false);
        ButterKnife.inject(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLists = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mLists.add("北京--" + i + "--天安门");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        searchRlvNearbypoi.setItemAnimator(new DefaultItemAnimator());
        searchRlvNearbypoi.addItemDecoration(new DividerItemDecoration(mActivity,HORIZONTAL));
        searchRlvNearbypoi.setLayoutManager(layoutManager);
        mAdapter = new SearchNearbyPoiRecyclerViewAdapter(mLists);
        searchRlvNearbypoi.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
