package com.qin.fragment.drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qin.R;
import com.qin.fragment.BaseFragment;
import com.qin.view.textview.RunTextViewVertical;
import com.qin.view.textview.RunTextViewVerticalMore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class UseGuideFragment extends BaseFragment {
    @BindView(R.id.tv_main_location)
    TextView tvMainLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.iv_main_topbackgraound)
    ImageView ivMainTopbackgraound;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_main_breakrule_happen)
    TextView tvMainBreakruleHappen;
    @BindView(R.id.tv_main_breakrule)
    RunTextViewVerticalMore tvMainBreakrule;
    @BindView(R.id.ll_main_breakrule)
    LinearLayout llMainBreakrule;
    @BindView(R.id.tv_main_qirquality)
    TextView tvMainQirquality;
    @BindView(R.id.tv_main_measure)
    TextView tvMainMeasure;
    @BindView(R.id.tv_main_todaygasprice)
    RunTextViewVertical tvMainTodaygasprice;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar_tempbound)
    TextView toolbarTempbound;
    @BindView(R.id.toolbar_advice)
    RunTextViewVertical toolbarAdvice;
    @BindView(R.id.ll_main_gas_station)
    LinearLayout llMainGasStation;
    @BindView(R.id.ll_main_parking)
    LinearLayout llMainParking;
    @BindView(R.id.ll_main_repair_shop)
    LinearLayout llMainRepairShop;
    @BindView(R.id.ll_main_repair_wash)
    LinearLayout llMainRepairWash;
    @BindView(R.id.ll_main_service)
    LinearLayout llMainService;
    @BindView(R.id.ll_main_vehicleoffice)
    LinearLayout llMainVehicleoffice;
    @BindView(R.id.ll_main_toilet)
    LinearLayout llMainToilet;
    @BindView(R.id.ll_main_maintain)
    LinearLayout llMainMaintain;
    @BindView(R.id.tv_main_trafficmore)
    TextView tvMainTrafficmore;
    @BindView(R.id.banner_traffic)
    Banner bannerTraffic;
    @BindView(R.id.tv_breakrule)
    TextView tvBreakrule;
    @BindView(R.id.ll_main_breakrule_check)
    LinearLayout llMainBreakruleCheck;
    @BindView(R.id.tv_breakrulehappen)
    TextView tvBreakrulehappen;
    @BindView(R.id.ll_main_breakrule_happen)
    LinearLayout llMainBreakruleHappen;
    @BindView(R.id.tv_endcodlimited)
    TextView tvEndcodlimited;
    @BindView(R.id.ll_main_limitd)
    LinearLayout llMainLimitd;
    @BindView(R.id.tv_vinanalysis)
    TextView tvVinanalysis;
    @BindView(R.id.ll_main_headingcode)
    LinearLayout llMainHeadingcode;
    @BindView(R.id.tv_cartype)
    TextView tvCartype;
    @BindView(R.id.ll_main_cartype)
    LinearLayout llMainCartype;
    @BindView(R.id.tv_platechenck)
    TextView tvPlatechenck;
    @BindView(R.id.ll_main_platechck)
    LinearLayout llMainPlatechck;
    @BindView(R.id.tv_breakdowndtc)
    TextView tvBreakdowndtc;
    @BindView(R.id.ll_main_breakdown)
    LinearLayout llMainBreakdown;
    @BindView(R.id.tv_carinsurance)
    TextView tvCarinsurance;
    @BindView(R.id.ll_main_carinsurance)
    LinearLayout llMainCarinsurance;
    @BindView(R.id.tv_main_carcotrolmore)
    TextView tvMainCarcotrolmore;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    Unbinder unbinder;

    @Override
    protected void initData() {

    }

    @Override
    public View initView() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useguide, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_main_maintain)
    public void onViewClicked() {

    }


}
