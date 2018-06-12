package com.qin.fragment.main.owner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qin.R;
import com.qin.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class CarTypeFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.fl_main_cartype)
    FrameLayout flMainCartype;
    private CarBrandFragment mCarBrandFragment;

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_cartype, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

        android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        mCarBrandFragment = new CarBrandFragment();
        transaction.replace(R.id.fl_main_cartype, mCarBrandFragment).commit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                if (mCarBrandFragment.isVisible()){
                    mActivity.finish();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
