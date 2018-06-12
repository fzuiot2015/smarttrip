package com.qin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qin.R;
import com.qin.adapter.recyclerview.LocationHistoryRecyclerViewAdapter;
import com.qin.adapter.recyclerview.LocationNearbyPoiRecyclerViewAdapter;
import com.qin.adapter.recyclerview.MarginDecoration;
import com.qin.util.SPUtils;
import com.qin.util.ToastUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class LocationActivity extends AppCompatActivity {


    @BindView(R.id.tv_location_location)
    TextView tvLocationLocation;
    @BindView(R.id.et_location_location)
    EditText etLocationLocation;
    @BindView(R.id.iv_location_ok)
    ImageView ivLocationOk;
    @BindView(R.id.tv_location_currentlosition)
    TextView tvLocationCurrentlosition;
    @BindView(R.id.iv_location_agiainloc)
    ImageView ivLocationAgiainloc;
    @BindView(R.id.tv_location_agiainloc)
    TextView tvLocationAgiainloc;
    @BindView(R.id.tv_location_clear)
    TextView tvLocationClear;
    @BindView(R.id.rlv_location_history)
    RecyclerView rlvLocationHistory;
    @BindView(R.id.tv_location_historynull)
    TextView tvLocationHistorynull;
    @BindView(R.id.rlv_location_nearbypoi)
    RecyclerView rlvLocationNearbypoi;

    private LinearLayoutManager mLayoutManager;
    private boolean isLocationSuccessful = false;
    private List<String> mPoiLists = new ArrayList<>();
    private List<String> mHistoryLists = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;
    private LocationHistoryRecyclerViewAdapter mHistoryAdapter;
    private LocationNearbyPoiRecyclerViewAdapter mAdapter;
    private Handler mHandler = new Handler();
    private Gson gson = new Gson();
    private LinearLayoutManager mHistorymanager;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption locationOption = null;
    private boolean isAgainLoc = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        tvLocationCurrentlosition.setSelected(true);
        Bundle bundle = getIntent().getExtras();
        String currentLocation = bundle.getString("currentLocation", "无法获取位置信息");
        String currentLocationcity = bundle.getString("currentLocationcity", "无法获取位置信息");

        //获取保存入sp中的值
        String json = SPUtils.getInstance(this).getString("location_history", "");
        /**
         * 解析保存入sp中的list集合
         */
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> historylocation = gson.fromJson(json, type);
        //添加到集合

        //设置城市名和当前地名
        tvLocationCurrentlosition.setText(currentLocation);
        tvLocationLocation.setText(currentLocationcity);

        initLocation();
        //    initData();
        /**
         * recyclerview布局管理器
         */
        mLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mHistorymanager = new LinearLayoutManager(this);
        /**
         * 附近poi显示
         */
        rlvLocationNearbypoi.setItemAnimator(new DefaultItemAnimator());
        rlvLocationNearbypoi.setLayoutManager(mLayoutManager);
        rlvLocationNearbypoi.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new LocationNearbyPoiRecyclerViewAdapter(mPoiLists);
        rlvLocationNearbypoi.setAdapter(mAdapter);
        /**
         *历史位置显示
         */
        rlvLocationHistory.setItemAnimator(new DefaultItemAnimator());
        rlvLocationHistory.setLayoutManager(mHistorymanager);
        rlvLocationHistory.addItemDecoration(new MarginDecoration(10));
//        rlvLocationHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mHistoryAdapter = new LocationHistoryRecyclerViewAdapter(mHistoryLists);
        if (historylocation != null) {
            tvLocationHistorynull.setVisibility(View.GONE);
            //TODO  添加到集合，向后添加
            mHistoryLists.addAll(historylocation);
        } else {
            tvLocationHistorynull.setVisibility(View.VISIBLE);
        }
        rlvLocationHistory.setAdapter(mHistoryAdapter);

        mHistoryAdapter.setOnItemClickListener(new LocationHistoryRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                RelativeLayout layout = (RelativeLayout) mHistorymanager.findViewByPosition(postion);
                TextView text = (TextView) layout.getChildAt(0);
                etLocationLocation.setText(text.getText().toString().trim());
            }
        });
        mAdapter.setOnItemClickListener(new LocationNearbyPoiRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LocationNearbyPoiRecyclerViewAdapter.ViewName viewName, int postion) {
                if (LocationNearbyPoiRecyclerViewAdapter.ViewName.ITEM == viewName) {
                    RelativeLayout layout = (RelativeLayout) mLayoutManager.findViewByPosition(postion);
                    TextView text = (TextView) layout.getChildAt(0);
                    etLocationLocation.setText(text.getText().toString().trim());
                }
                if (LocationNearbyPoiRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                    ToastUtils.showBgResource(LocationActivity.this, "导航到" + postion);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationOption = null;
        }
    }

    @OnClick({R.id.iv_location_ok, R.id.tv_location_agiainloc, R.id.tv_location_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location_ok:
                String editTextName = etLocationLocation.getText().toString().trim();
                Log.i("", "---------editTextName----------" + editTextName);
                Intent intent = new Intent();
                if (null != editTextName && "" != editTextName) {
                    intent.putExtra("editTextName", editTextName);
                    setResult(RESULT_OK, intent);
                    mHistoryLists.add(editTextName);
                    HashSet h = new HashSet(mHistoryLists);
                    mHistoryLists.clear();
                    mHistoryLists.addAll(h);
                    String json = gson.toJson(mHistoryLists);
                    SPUtils.getInstance(this).putString("location_history", json, true);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();

                break;
            case R.id.tv_location_agiainloc:
                startLocation();
                tvLocationAgiainloc.setText("正在定位...");
                break;
            case R.id.tv_location_clear:
                mHistoryAdapter.removeData();
                rlvLocationHistory.setAdapter(mHistoryAdapter);
                tvLocationHistorynull.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * 设置当前位置
     * @param text
     */
    public void setTvCurrentLocationText(final String text) {
        if (tvLocationCurrentlosition != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    tvLocationCurrentlosition.post(new Runnable() {
                        @Override
                        public void run() {
                            tvLocationCurrentlosition.setText(text);
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * 跳转到activity
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LocationActivity.class);
        context.startActivity(intent);
    }

    /**
     * 初始化位置信息
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        mLocationClient.setLocationOption(locationOption);
        // 设置定位监听
        mLocationClient.setLocationListener(locationListener);
    }

    private void startLocation() {
        // 启动定位
        mLocationClient.startLocation();
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    Log.i("location", location.getAddress());
                    setTvCurrentLocationText(location.getAddress());
                    etLocationLocation.setText(location.getAddress());
                    String poiName = location.getPoiName();
                    mPoiLists.clear();
                    mPoiLists.add(poiName);
                } else {
                    setTvCurrentLocationText("定位失败");
                }
                tvLocationAgiainloc.setText("重新定位");
                isAgainLoc = false;
            }
        }
    };
}
