package com.qin.activity.nearby;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.qin.R;
import com.qin.adapter.viewpager.GasViewPagerAdapter;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.fragment.main.gas.AllGasFragment;
import com.qin.util.NetworkUtil;
import com.weavey.loading.lib.LoadingLayout;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/30 0030.
 */

public class GasStationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    LoadingLayout loadinglayout;
    @BindView(R.id.indicator_gas_title)
    MagicIndicator indicatorGasTitle;
    @BindView(R.id.vp_gas)
    ViewPager vpGas;

    //private static final String[] CHANNELS = new String[]{"全部", "中国石化","中国石油", "民营", "其它"};
    private static final String[] CHANNELS = new String[]{"全部"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private int number;
    private NearbyNetworkChangedReceiver mNetworkChangedReceiver;
    public  double mLat;
    public  double mLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasstation);
        ButterKnife.bind(this);
        loadinglayout = findViewById(R.id.loadinglayout);
        mNetworkChangedReceiver = new NearbyNetworkChangedReceiver(loadinglayout);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangedReceiver, intentFilter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("加油站");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLat = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LAT);
        mLon = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LON);

        Log.i("lat",mLat+"gas"+mLon);

        loadinglayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                new AllGasFragment().accessNet(1,3000,0);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadinglayout.setStatus(LoadingLayout.Success);
                initDatas();
            }
        }, 1);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        List<BaseFragment> lists = new ArrayList<>();
        lists.add(new AllGasFragment());
//        lists.add(new ParkingFragment());
//        lists.add(new ParkingFragment1());
//        lists.add(new ParkingFragment2());
//        lists.add(new ParkingFragment2());
        vpGas.setAdapter(new GasViewPagerAdapter(getSupportFragmentManager(), lists));
        initMagicIndicator(indicatorGasTitle, mDataList);
    }

    /**
     * 初始化MagicIndicator
     * @param indicator
     * @param list
     */
    private void initMagicIndicator(MagicIndicator indicator, final List<String> list) {
        indicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.2f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vpGas.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                // setup badge
                if (index != 1 || index != 3) {
                    ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_red_dot_badge, null);
                    badgePagerTitleView.setBadgeView(badgeImageView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_LEFT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
                }
                // cancel badge when click tab, default true
                badgePagerTitleView.setAutoCancelBadge(true);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(Color.parseColor("#ebe4e3"));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        final FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper(indicator);
        fragmentContainerHelper.setInterpolator(new OvershootInterpolator(2.0f));
        fragmentContainerHelper.setDuration(300);
        vpGas.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                fragmentContainerHelper.handlePageSelected(position);
            }
        });
    }

    /**
     * 跳转到加油界面
     * @param context
     * @param intent
     * @param number
     */
    public static void startActivity(Context context, Intent intent, int number) {
        intent.putExtra(ConstantValues.NEARBY_NUMBER, number);
        intent.setClass(context, GasStationActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转界面
     * @param context
     * @param intent
     * @param number
     * @param lat
     * @param lon
     */
    public static void startActivityWithParams(Context context, Intent intent, int number, double lat, double lon) {
        intent.putExtra(ConstantValues.NEARBY_NUMBER, number);
        intent.putExtra(ConstantValues.NEARBY_LAT, lat);
        intent.putExtra(ConstantValues.NEARBY_LON, lon);
        intent.setClass(context, GasStationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNetworkChangedReceiver);
        super.onDestroy();
    }

    /**
     * 网络监听服务
     */
    public class NearbyNetworkChangedReceiver extends BroadcastReceiver {
        private LoadingLayout mLoadingLayout;

        public NearbyNetworkChangedReceiver(LoadingLayout loadingLayout) {
            this.mLoadingLayout = loadingLayout;
        }

        @Override

        public void onReceive(Context context, Intent intent) {
            int netWorkStates = NetworkUtil.getNetWorkStates(context);

            switch (netWorkStates) {
                case NetworkUtil.TYPE_NONE:
                    //断网
                    mLoadingLayout.setStatus(LoadingLayout.No_Network);
                    break;
                case NetworkUtil.TYPE_SUCCESS:
                    //有网络
                    loadinglayout.setStatus(LoadingLayout.Success);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onAttachedToWindow() {
        try {
            super.onAttachedToWindow();
        } catch (IllegalStateException e) {

        }
    }
}
