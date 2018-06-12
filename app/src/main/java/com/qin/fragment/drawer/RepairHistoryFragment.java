package com.qin.fragment.drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.R;
import com.qin.fragment.BaseFragment;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class RepairHistoryFragment extends BaseFragment {
    @Override
    protected void initData() {
        Log.i("viewpager",this
                .getClass().getName());
    }

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repairhistory, null, false);
        return view;
    }
}
