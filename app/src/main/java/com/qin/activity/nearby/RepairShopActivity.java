package com.qin.activity.nearby;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.usersearch.RepairShopRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.up.parking.UpParking;
import com.qin.pojo.usersearch.repairshop.Contents;
import com.qin.pojo.usersearch.repairshop.RepairShop;
import com.qin.util.NetworkUtil;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/6 0006.
 */

public class RepairShopActivity extends AppCompatActivity implements
        View.OnClickListener,
        OnRefreshLoadMoreListener, INaviInfoCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search_nearbypoi_about)
    ImageView ivSearchNearbypoiAbout;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.ll_repairshop_about)
    LinearLayout mLlRepairshopAbout;
    @BindView(R.id.tv_repairshop_distance)
    TextView mTvRepairshopDistance;
    @BindView(R.id.tv_repairshop_score)
    TextView mTvRepairshopScore;
    @BindView(R.id.rlv_repairshop)
    RecyclerView mRlvRepairshop;
    @BindView(R.id.refresh_repairshop)
    SmartRefreshLayout mRefreshRepairshop;
    @BindView(R.id.tv_repairshop_no)
    TextView mTvRepairshopNo;

    private View popView;
    private int popIsShowing = 1;

    private DisplayMetrics mDisplayMetrics;
    private int mWidthPixels;
    private int mHeightPixels;

    public PopupWindow popupWindow;
    private RelativeLayout mRl_pop_bound1;
    private RelativeLayout mRl_pop_bound2;
    private RelativeLayout mRl_pop_bound3;
    private RelativeLayout mRl_pop_bound4;
    private RelativeLayout mRl_pop_bound5;
    private ImageView ivPop1;
    private ImageView ivPop2;
    private ImageView ivPop3;
    private ImageView ivPop4;
    private ImageView ivPop5;
    private RepairShopRecyclerViewAdapter mAdapter;
    private double mLatitude;
    private double mLongitude;
    int firstPage = 1;
    private Dialog mDialog;
    private NearbyNetworkChangedReceiver mNetworkChangedReceiver;
    private int mBoundFlag = 3;
    private int mSortFlag = 1;
    private boolean isScore = false;
    private boolean isDescendingDistance = false;
    private AmapTTSController amapTTSController;
    private List<Contents> mResultsList;
    private int currentPage = 0;
    private int mSize;
    private int mTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairshop);
        ButterKnife.bind(this);
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDisplayMetrics = new DisplayMetrics();
        initDialog(this);
        mNetworkChangedReceiver = new NearbyNetworkChangedReceiver(loadingLayout);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangedReceiver, intentFilter);
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWidthPixels = mDisplayMetrics.widthPixels;
        mHeightPixels = mDisplayMetrics.heightPixels;

        mLatitude = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LAT, 119.203488);
        mLongitude = getIntent().getExtras().getDouble(ConstantValues.NEARBY_LON, 26.062197);
        Log.i("repairshop", mLatitude + "---" + mLongitude);

        mTvRepairshopDistance.setTextColor(getResources().getColor(R.color.red));
        mRefreshRepairshop.setOnRefreshLoadMoreListener(this);
        mRefreshRepairshop.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);

        initPopWindow(this, R.layout.popwindow_search_bound, mWidthPixels - 60, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRlvRepairshop.setItemAnimator(new DefaultItemAnimator());
        mRlvRepairshop.addItemDecoration(new MarginDecorationVertical(20));
        mRlvRepairshop.setLayoutManager(layoutManager);
        accessNet(2000, "distance:1");
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                //TODO 出错界面重新加载
                if (mBoundFlag == 1) {
                    judgeSortby(500);
                } else if (mBoundFlag == 2) {
                    judgeSortby(1000);
                } else if (mBoundFlag == 3) {
                    judgeSortby(2000);
                } else if (mBoundFlag == 4) {
                    judgeSortby(5000);
                } else if (mBoundFlag == 5) {
                    judgeSortby(1000);
                }
            }
        });
    }

    @OnClick({R.id.ll_repairshop_about, R.id.tv_repairshop_score, R.id.tv_repairshop_distance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_repairshop_about:
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                    ivSearchNearbypoiAbout.setImageResource(R.mipmap.up_login);
                } else {
                    popupWindow.showAsDropDown(mLlRepairshopAbout, -24, 20);
                    setWindowGray(0.7f);
                    ivSearchNearbypoiAbout.setImageResource(R.mipmap.down_login);
                }
                break;
            case R.id.tv_repairshop_distance:
                mSortFlag = 1;
                judgeBound("distance:1");
                mTvRepairshopDistance.setTextColor(getResources().getColor(R.color.red));
                mTvRepairshopScore.setTextColor(Color.GRAY);
                break;
            case R.id.tv_repairshop_score:
                mSortFlag = 2;
                judgeBound("score:1");
                mTvRepairshopDistance.setTextColor(Color.GRAY);
                mTvRepairshopScore.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }

    private void judgeBound(String sortby) {
        if (mBoundFlag == 1) {
            accessNet(500, sortby);
        } else if (mBoundFlag == 2) {
            accessNet(1000, sortby);
        } else if (mBoundFlag == 3) {
            accessNet(2000, sortby);
        } else if (mBoundFlag == 4) {
            accessNet(5000, sortby);
        } else if (mBoundFlag == 5) {
            accessNet(10000, sortby);
        }
    }

    private void initDialog(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(this), ScreenUtils.getWindowHeight(this));
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        params.height = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        mDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    public static void startActivityWithParams(Context context, Intent intent, int number, double lat, double lon) {
        intent.putExtra(ConstantValues.NEARBY_NUMBER, number);
        intent.putExtra(ConstantValues.NEARBY_LAT, lat);
        intent.putExtra(ConstantValues.NEARBY_LON, lon);
        intent.setClass(context, RepairShopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void initListener() {
        mRl_pop_bound1 = popView.findViewById(R.id.rl_pop_bound1);
        mRl_pop_bound2 = popView.findViewById(R.id.rl_pop_bound2);
        mRl_pop_bound3 = popView.findViewById(R.id.rl_pop_bound3);
        mRl_pop_bound4 = popView.findViewById(R.id.rl_pop_bound4);
        mRl_pop_bound5 = popView.findViewById(R.id.rl_pop_bound5);
        ivPop1 = popView.findViewById(R.id.iv_pop1);
        ivPop2 = popView.findViewById(R.id.iv_pop2);
        ivPop3 = popView.findViewById(R.id.iv_pop3);
        ivPop4 = popView.findViewById(R.id.iv_pop4);
        ivPop5 = popView.findViewById(R.id.iv_pop5);
        mRl_pop_bound1.setOnClickListener(this);
        mRl_pop_bound2.setOnClickListener(this);
        mRl_pop_bound3.setOnClickListener(this);
        mRl_pop_bound4.setOnClickListener(this);
        mRl_pop_bound5.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowGray(1.0f);
            }
        });
    }

    public void initPopWindow(Context context, int layoutRes, int width, int height) {
        popView = View.inflate(this, R.layout.popwindow_search_bound, null);
        popupWindow = new PopupWindow(popView, width, height, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);

        popIsShowing = 1;
        popView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        popupWindow.setAnimationStyle(R.style.popSearchAnimtion);

        initListener();
    }

    private void accessNet(int radius, String sortby) {
        mDialog.show();
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("汽车维修");
        upParking.setSortby(sortby);
        upParking.setPage_index(0 + "");
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_REPAIRSHOP).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.GONE);
//                refreshParking.setVisibility(View.VISIBLE);
                parseData(body);
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("repairshop", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    private void loadMoreAccessNet(int radius, int page, String sortby) {
        mDialog.show();
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("汽车维修");
        upParking.setSortby(sortby);
        upParking.setPage_index(page + "");
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_REPAIRSHOP).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                mDialog.dismiss();
                Gson gson = new Gson();
                RepairShop repairShop = gson.fromJson(body, RepairShop.class);
                int status = repairShop.getStatus();
                if (status == 0) {
                    List<Contents> contents = repairShop.getContents();
                    if (contents.size() > 0) {
                        mResultsList.addAll(contents);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showBgResource(RepairShopActivity.this, "已加载所有数据");
                    }
                }
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("repairshop", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    private void refreshAccessNet(int radius, String sortby) {
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("汽车维修");
        upParking.setSortby(sortby);
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_REPAIRSHOP).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                parseData(body);
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("repairshop", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    private void parseData(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("0")) {
                Gson gson = new Gson();
                RepairShop repairShop = gson.fromJson(json, RepairShop.class);
                mSize = repairShop.getSize();
                mTotal = repairShop.getTotal();
                mResultsList = repairShop.getContents();
                if (mResultsList.size() > 0) {
                    mTvRepairshopNo.setVisibility(View.GONE);
                    mRefreshRepairshop.setVisibility(View.VISIBLE);
                    mAdapter = new RepairShopRecyclerViewAdapter(mResultsList);
                    mRlvRepairshop.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setOnItemClickListener(new RepairShopRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RepairShopRecyclerViewAdapter.ViewName viewName, int postion) {
                            double lat = Double.parseDouble(mResultsList.get(postion).getLocation().get(1));
                            double lng = Double.parseDouble(mResultsList.get(postion).getLocation().get(0));
                            //         com.baidu.mapapi.model.LatLng latLng = new com.baidu.mapapi.model.LatLng(lat,lng);
                            if (RepairShopRecyclerViewAdapter.ViewName.ITEM == viewName) {
//                            Intent intent = new Intent();
//                            intent.putExtra("latlng", latLng);
//                            intent.setClass(RepairShopActivity.this, BaiduMapPoiActivity.class);
//                            startActivity(intent);
                            }
                            if (RepairShopRecyclerViewAdapter.ViewName.IMAGEVIEW_NA == viewName) {
                                AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                                        , new AmapNaviParams(new Poi(null, null, "")
                                                , null
                                                , new Poi(mResultsList.get(postion).getTitle(), new LatLng(
                                                lat
                                                , lng), "")
                                                , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                        , RepairShopActivity.this);
                            } else if (RepairShopRecyclerViewAdapter.ViewName.IMAGEVIEW_PHONE == viewName) {
                                long phone = mResultsList.get(postion).getGarage_tel();
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + phone);
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    mTvRepairshopNo.setVisibility(View.VISIBLE);
                    mRefreshRepairshop.setVisibility(View.GONE);
                }
            }else{
                ToastUtils.showBgResource(RepairShopActivity.this, "服务器异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showBgResource(RepairShopActivity.this, "解析异常");
        }
    }

    public void setWindowGray(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        currentPage = 0;
        switch (v.getId()) {
            case R.id.rl_pop_bound1:
                mBoundFlag = 1;
                judgeSortby(500);
                mRlvRepairshop.setAdapter(mAdapter);
                ivPop1.setVisibility(View.VISIBLE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound2:
                mBoundFlag = 2;
                judgeSortby(1000);
                mRlvRepairshop.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.VISIBLE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound3:
                mBoundFlag = 3;
                judgeSortby(2000);
                mRlvRepairshop.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.VISIBLE);
                ivPop4.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound4:
                mBoundFlag = 4;
                judgeSortby(5000);
                mRlvRepairshop.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.VISIBLE);
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound5:
                mBoundFlag = 5;
                judgeSortby(10000);
                mRlvRepairshop.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void judgeSortby(int radius) {
        if (mSortFlag == 1) {
            accessNet(radius, "distance:1");
        } else if (mSortFlag == 2) {
            accessNet(radius, "score:1");
        }
    }

    private void judgeSortbyRefresh(int radius) {
        if (mSortFlag == 1) {
            refreshAccessNet(radius, "distance:1");
        } else if (mSortFlag == 2) {
            refreshAccessNet(radius, "score:1");
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (mTotal > mSize) {
            int totalPage = mTotal / mSize + 1;
            if (currentPage < totalPage) {
                currentPage++;
                if (mSortFlag == 1) {
                    loadMoreAccessNet(10000, currentPage, "distance:1");
                } else if (mSortFlag == 2) {
                    loadMoreAccessNet(10000, currentPage, "score:1");
                }
            }
        }
        mRefreshRepairshop.finishLoadMore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (mBoundFlag == 1) {
            judgeSortbyRefresh(500);
        } else if (mBoundFlag == 2) {
            judgeSortbyRefresh(1000);
        } else if (mBoundFlag == 3) {
            judgeSortbyRefresh(2000);
        } else if (mBoundFlag == 4) {
            judgeSortbyRefresh(5000);
        } else if (mBoundFlag == 5) {
            judgeSortbyRefresh(10000);
        }
        mRefreshRepairshop.finishRefresh();
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {

        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {

        amapTTSController.stopSpeaking();
    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

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
                    ToastUtils.showBgResource(context, "亲，您的网络出错啦！");
                    mLoadingLayout.setStatus(LoadingLayout.No_Network);
                    break;
                case NetworkUtil.TYPE_SUCCESS:
                    //有网络
                    //                   loadingLayout.setStatus(LoadingLayout.Success);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        unregisterReceiver(mNetworkChangedReceiver);
    }
}
