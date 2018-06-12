package com.qin.activity.nearby;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
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
import com.qin.adapter.recyclerview.usersearch.ParkingRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.up.parking.UpParking;
import com.qin.pojo.usersearch.parking.Contents;
import com.qin.pojo.usersearch.parking.Parking;
import com.qin.util.NetworkUtil;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.weavey.loading.lib.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/6 0006.
 */

public class ParkingActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshLoadMoreListener, INaviInfoCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search_nearbypoi_about)
    ImageView ivSearchNearbypoiAbout;
    @BindView(R.id.ll_parking_about)
    LinearLayout llParkingAbout;
    @BindView(R.id.tv_parking_distance)
    TextView tvParkingDistance;
    @BindView(R.id.rlv_parking)
    RecyclerView rlvParking;
    @BindView(R.id.refresh_parking)
    SmartRefreshLayout refreshParking;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.tv_parking_price)
    TextView mTvParkingPrice;
    @BindView(R.id.tv_parking_empty)
    TextView mTvParkingEmpty;
    @BindView(R.id.tv_parking_no)
    TextView mTvParkingNo;
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
    private ParkingRecyclerViewAdapter mAdapter;
    private double mLatitude;
    private double mLongitude;
    private Dialog mDialog;
    private NearbyNetworkChangedReceiver mNetworkChangedReceiver;
    private List<Contents> mContentsList;
    private int mBoundFlag = 5;
    private int mSortFlag = 1;
    private int currentPage = 0;
    private boolean isDescendingPrice = false;
    private boolean isDescendingDistance = false;
    private boolean isDescendingEmpty = false;
    private AmapTTSController amapTTSController;
    private int mSize;
    private int mTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
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
        Log.i("parking", mLatitude + "---" + mLongitude);
        tvParkingDistance.setTextColor(getResources().getColor(R.color.red));
        refreshParking.setOnRefreshLoadMoreListener(this);
        refreshParking.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);

        initPopWindow(this, R.layout.popwindow_search_bound, mWidthPixels - 60, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlvParking.setItemAnimator(new DefaultItemAnimator());
        //  rlvParking.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rlvParking.addItemDecoration(new MarginDecorationVertical(20));
        rlvParking.setLayoutManager(layoutManager);
        accessNetParking(10000, "distance:1");
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

    @OnClick({R.id.ll_parking_about, R.id.tv_parking_price, R.id.tv_parking_empty, R.id.tv_parking_distance})
    public void onViewClicked(View view) {
        currentPage = 0;
        switch (view.getId()) {
            case R.id.ll_parking_about:
                ToastUtils.showBgResource(this, "fanwei");
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                    ivSearchNearbypoiAbout.setImageResource(R.mipmap.up_login);
                } else {
                    popupWindow.showAsDropDown(llParkingAbout, -24, 20);
                    setWindowGray(0.7f);
                    ivSearchNearbypoiAbout.setImageResource(R.mipmap.down_login);
                }
                break;
            case R.id.tv_parking_price:
                mSortFlag = 1;
                if (!isDescendingPrice) {
                    judgeBound("parking_charge:1");
                } else {
                    judgeBound("parking_charge:-1");
                }
                isDescendingPrice = !isDescendingPrice;
                tvParkingDistance.setTextColor(Color.GRAY);
                mTvParkingEmpty.setTextColor(Color.GRAY);
                mTvParkingPrice.setTextColor(getResources().getColor(R.color.red));
                break;
            case R.id.tv_parking_empty:
                mSortFlag = 2;
                if (!isDescendingEmpty) {
                    judgeBound("parking_rest:1");
                } else {
                    judgeBound("parking_rest:-1");
                }
                isDescendingEmpty = !isDescendingEmpty;
                tvParkingDistance.setTextColor(Color.GRAY);
                mTvParkingPrice.setTextColor(Color.GRAY);
                mTvParkingEmpty.setTextColor(getResources().getColor(R.color.red));
                break;
            case R.id.tv_parking_distance:
                mSortFlag = 3;
                if (!isDescendingDistance) {
                    judgeBound("distance:1");
                } else {
                    judgeBound("distance:-1");
                }
                isDescendingDistance = !isDescendingDistance;
                tvParkingDistance.setTextColor(getResources().getColor(R.color.red));
                mTvParkingEmpty.setTextColor(Color.GRAY);
                mTvParkingPrice.setTextColor(Color.GRAY);
                break;
        }
    }

    /**
     * 根据范围跳转
     *
     * @param sortby
     */
    private void judgeBound(String sortby) {
        if (mBoundFlag == 1) {
            accessNetParking(500, sortby);
        } else if (mBoundFlag == 2) {
            accessNetParking(1000, sortby);
        } else if (mBoundFlag == 3) {
            accessNetParking(2000, sortby);
        } else if (mBoundFlag == 4) {
            accessNetParking(5000, sortby);
        } else if (mBoundFlag == 5) {
            accessNetParking(10000, sortby);
        }
    }

    /**
     * 初始化对话框
     *
     * @param context
     */
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
        intent.setClass(context, ParkingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 初始化监听器类
     */
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

    /**
     * 初始化popwindow
     *
     * @param context
     * @param layoutRes
     * @param width
     * @param height
     */
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

    private void accessNetParking(int radius, String sortby) {
        mDialog.show();
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("停车场");
        upParking.setPage_index(0 + "");
        upParking.setSortby(sortby);
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_PARKING).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("parking", body);
                Log.i("parking", "onSuccess");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.GONE);
//                refreshParking.setVisibility(View.VISIBLE);
                parseDataParking(body);
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("parking", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    /**
     * 加载更多停车场数据
     *
     * @param radius
     * @param page
     * @param sortby
     */
    private void loadMoreAccessNetParking(int radius, int page, String sortby) {
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("停车场");
        upParking.setPage_index(page + "");
        upParking.setSortby(sortby);
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_PARKING).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("parking", body);
                Log.i("parking", "onSuccess");
                Gson gson = new Gson();
                Parking parking = gson.fromJson(body, Parking.class);
                int status = parking.getStatus();
                if (status == 0) {
                    List<Contents> contents = parking.getContents();
                    if (contents.size() > 0) {
                        mContentsList.addAll(contents);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showBgResource(ParkingActivity.this, "已加载所有数据");
                    }
                }
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("parking", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    /**
     * 下拉刷新获取停车场数据
     *
     * @param radius
     * @param sortby
     */
    private void refreshAccessNetParking(int radius, String sortby) {
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("停车场");
        upParking.setPage_index(0 + "");
        upParking.setSortby(sortby);
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_PARKING).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("parking", body);
                Log.i("parking", "onSuccess");
                mDialog.dismiss();
                try {
                    parseDataParking(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadingLayout.setStatus(LoadingLayout.Success);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("parking", "onError");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.VISIBLE);
//                refreshParking.setVisibility(View.GONE);
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    /**
     * 解析停车场数据
     *
     * @param json
     */
    private void parseDataParking(String json) {
        Gson gson = new Gson();
        Parking parking = gson.fromJson(json, Parking.class);
        int status = parking.getStatus();
        if (status == 0) {
            mSize = parking.getSize();
            mTotal = parking.getTotal();
            mContentsList = parking.getContents();
            if (mContentsList.size() > 0) {
                mTvParkingNo.setVisibility(View.GONE);
                refreshParking.setVisibility(View.VISIBLE);
                mAdapter = new ParkingRecyclerViewAdapter(mContentsList);
                rlvParking.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new ParkingRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, ParkingRecyclerViewAdapter.ViewName viewName, int postion) {
                        if (ParkingRecyclerViewAdapter.ViewName.ITEM == viewName) {
//                            ToastUtils.showBgResource(ParkingActivity.this, "" + postion);
//                            String name = mContentsList.get(postion).getTitle();
//                            Intent intent = new Intent();
//                            intent.putExtra(ConstantValues.PARKING_POI, name);
//                            intent.putExtra(ConstantValues.PARKING_LAT, mContentsList.get(postion).getLocation().get(1));
//                            intent.putExtra(ConstantValues.PARKING_LON, mContentsList.get(postion).getLocation().get(0));
//                            String lng = mContentsList.get(0).getLocation().get(0);
//                            String lat = mContentsList.get(0).getLocation().get(1);
//                            Log.i("lat", lat + "------------------" + lng + "====" + mContentsList.get(postion).getParking_rest_location());
//                            intent.setClass(ParkingActivity.this, ParkingSeatActivity.class);
//                            startActivity(intent);
                        }
                        if (ParkingRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                                    , new AmapNaviParams(new Poi(null, null, "")
                                            , null
                                            , new Poi(mContentsList.get(postion).getTitle(), new LatLng(Double.parseDouble(mContentsList.get(0).getLocation().get(1)), Double.parseDouble(mContentsList.get(0).getLocation().get(0))), "")
                                            , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                    , ParkingActivity.this);
                        }
                    }
                });
            } else {
                mTvParkingNo.setVisibility(View.VISIBLE);
                refreshParking.setVisibility(View.GONE);
            }
        } else {
            ToastUtils.showBgResource(ParkingActivity.this, "服务器异常");
        }
    }

    /**
     * 设置屏幕亮度
     *
     * @param f
     */
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
                rlvParking.setAdapter(mAdapter);
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
                rlvParking.setAdapter(mAdapter);
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
                rlvParking.setAdapter(mAdapter);
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
                rlvParking.setAdapter(mAdapter);
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
                rlvParking.setAdapter(mAdapter);
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
            accessNetParking(radius, "parking_charge:1");
        } else if (mSortFlag == 2) {
            accessNetParking(radius, "parking_rest:1");
        } else if (mSortFlag == 3) {
            accessNetParking(radius, "distance:1");
        }
    }

    private void judgeSortbyRefresh(int radius) {
        if (mSortFlag == 1) {
            refreshAccessNetParking(radius, "parking_charge:1");
        } else if (mSortFlag == 2) {
            refreshAccessNetParking(radius, "parking_rest:1");
        } else if (mSortFlag == 3) {
            refreshAccessNetParking(radius, "distance:1");
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (mTotal > mSize) {
            int totalPage = mTotal / mSize + 1;
            if (currentPage < totalPage) {
                currentPage++;
                if (mSortFlag == 1) {
                    loadMoreAccessNetParking(10000, currentPage, "parking_charge:1");
                } else if (mSortFlag == 2) {
                    loadMoreAccessNetParking(10000, currentPage, "parking_rest:1");
                } else if (mSortFlag == 3) {
                    loadMoreAccessNetParking(10000, currentPage, "distance:1");
                }
            }
        }
        refreshParking.finishLoadMore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage = 0;
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
        refreshParking.finishRefresh();
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
