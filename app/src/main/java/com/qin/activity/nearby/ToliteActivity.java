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
import com.qin.adapter.recyclerview.usersearch.ToiletRecyclerViewAdapter;
import com.qin.constant.ConstantValues;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.up.parking.UpParking;
import com.qin.pojo.usersearch.toilet.Results;
import com.qin.pojo.usersearch.toilet.Toilet;
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

public class ToliteActivity extends AppCompatActivity implements
        View.OnClickListener,
        OnRefreshLoadMoreListener, INaviInfoCallback {

    @BindView(R.id.iv_search_nearbypoi_about)
    ImageView mIvSearchNearbypoiAbout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rlv_toilet)
    RecyclerView mRlvToilet;
    @BindView(R.id.refresh_toilet)
    SmartRefreshLayout mRefreshToilet;
    @BindView(R.id.tv_toilet_no)
    TextView mTvToiletNo;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;
    @BindView(R.id.ll_toilet_about)
    LinearLayout mLlToiletAbout;

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
    private ToiletRecyclerViewAdapter mAdapter;
    private double mLatitude;
    private double mLongitude;
    private boolean isLoadMore = false;
    int firstPage = 0;
    private Dialog mDialog;
    private NearbyNetworkChangedReceiver mNetworkChangedReceiver;
    private AmapTTSController amapTTSController;
    private List<Results> mResultsList;
    private String mTotal;
    private int mPageNum;
    private int boundFlag = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tolite);
        ButterKnife.bind(this);
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        mRefreshToilet.setOnRefreshLoadMoreListener(this);
        mRefreshToilet.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);

        initPopWindow(this, R.layout.popwindow_search_bound, mWidthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRlvToilet.setItemAnimator(new DefaultItemAnimator());
        mRlvToilet.addItemDecoration(new MarginDecorationVertical(20));
        mRlvToilet.setLayoutManager(layoutManager);
        accessNetParking(10000, 0);
        loadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                //TODO 出错界面重新加载
                if (boundFlag == 1) {
                    accessNetParking(500, firstPage);
                } else if (boundFlag == 2) {
                    accessNetParking(1000, firstPage);
                } else if (boundFlag == 3) {
                    accessNetParking(2000, firstPage);
                } else if (boundFlag == 4) {
                    accessNetParking(5000, firstPage);
                } else if (boundFlag == 5) {
                    accessNetParking(10000, firstPage);
                }
            }
        });
    }

    @OnClick(R.id.ll_toilet_about)
    public void onViewClicked(View view) {
        isLoadMore = false;
        switch (view.getId()) {
            case R.id.ll_toilet_about:
                if (popIsShowing == 2) {
                    popupWindow.dismiss();
                    popIsShowing = 1;
                    mIvSearchNearbypoiAbout.setImageResource(R.mipmap.up_login);
                } else {
                    popupWindow.showAsDropDown(mLlToiletAbout, 0, 30);
                    setWindowGray(0.7f);
                    mIvSearchNearbypoiAbout.setImageResource(R.mipmap.down_login);
                }
                break;
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
        intent.setClass(context, ToliteActivity.class);
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

    private void accessNetParking(int radius, int pageNum) {
        mDialog.show();
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("公共厕所");
        upParking.setPage_num(pageNum + "");
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_TOLIET).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.GONE);
//                refreshParking.setVisibility(View.VISIBLE);
                parseDataParking(body);
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

    private void accessNetParkingLoadMore(int radius, int pageNum) {
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("公共厕所");
        upParking.setPage_num(pageNum + "");
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_TOLIET).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                loadingLayout.setStatus(LoadingLayout.Success);
                Gson gson = new Gson();
                Toilet toilet = gson.fromJson(body, Toilet.class);
                List<Results> results = toilet.getResults();
                if (results.size() == 0){
                    ToastUtils.showBgResource(ToliteActivity.this, "已加载所有数据");
                }else{
                    mResultsList.addAll(results);
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshToilet.finishLoadMore();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.i("repairshop", "onError");
                loadingLayout.setStatus(LoadingLayout.Error);
            }
        });
    }

    private void refreshAccessNetParking(int radius) {
        //TODO 实体类转换
        UpParking upParking = new UpParking();
        upParking.setLat(String.valueOf(mLatitude));
        upParking.setLng(String.valueOf(mLongitude));
        upParking.setRadius(radius + "");
        upParking.setTags("公共厕所");
        upParking.setPage_num(0 + "");
        Gson gson = new Gson();
        String json = gson.toJson(upParking);
        OkGo.<String>post(ConstantValues.URL_TOLIET).tag(this).upJson(json).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                Log.i("repairshop", body);
                Log.i("repairshop", "onSuccess");
                mDialog.dismiss();
//                tvParkingNo.setVisibility(View.GONE);
//                refreshParking.setVisibility(View.VISIBLE);
                parseDataParking(body);
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

    private void parseDataParking(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            mTotal = jsonObject.getString("total");
            mPageNum = Integer.parseInt(mTotal) / 10 + 1;
            Log.i("pageNum", "" + mPageNum);
            if (status.equals("0")) {
                Gson gson = new Gson();
                Toilet toilet = gson.fromJson(json, Toilet.class);
                mResultsList = toilet.getResults();
                //TODO  明天判断返回集合是否为空
                mAdapter = new ToiletRecyclerViewAdapter(mResultsList);
                mRlvToilet.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new ToiletRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, ToiletRecyclerViewAdapter.ViewName viewName, int postion) {
                        if (ToiletRecyclerViewAdapter.ViewName.ITEM == viewName) {
                            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                                    , new AmapNaviParams(new Poi(null, null, "")
                                            , null
                                            , new Poi(mResultsList.get(postion).getName(), new LatLng(
                                            mResultsList.get(postion).getLocation().getLat(), mResultsList.get(postion).getLocation().getLng()), "")
                                            , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                    , ToliteActivity.this);
                        }
                        if (ToiletRecyclerViewAdapter.ViewName.IMAGEVIEW == viewName) {
                            ToastUtils.showBgResource(ToliteActivity.this, "跳转" + postion);
                        }
                    }
                });
            } else {
                ToastUtils.showBgResource(ToliteActivity.this, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showBgResource(ToliteActivity.this, "获取异常");
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
        isLoadMore = false;
        firstPage = 0;
        switch (v.getId()) {
            case R.id.rl_pop_bound1:
                boundFlag = 1;
                accessNetParking(500, firstPage);
                mRlvToilet.setAdapter(mAdapter);
                ivPop1.setVisibility(View.VISIBLE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound2:
                boundFlag = 2;
                accessNetParking(1000, firstPage);
                mRlvToilet.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.VISIBLE);
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
            case R.id.rl_pop_bound3:
                boundFlag = 3;
                accessNetParking(2000, firstPage);
                mRlvToilet.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                ivPop3.setVisibility(View.VISIBLE);
                ivPop4.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound4:
                boundFlag = 4;
                accessNetParking(5000, firstPage);
                mRlvToilet.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.VISIBLE);
                ivPop5.setVisibility(View.GONE);
                break;
            case R.id.rl_pop_bound5:
                boundFlag = 5;
                accessNetParking(10000, firstPage);
                mRlvToilet.setAdapter(mAdapter);
                ivPop1.setVisibility(View.GONE);
                ivPop2.setVisibility(View.GONE);
                popupWindow.dismiss();
                ivPop3.setVisibility(View.GONE);
                ivPop4.setVisibility(View.GONE);
                ivPop5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (firstPage <= mPageNum) {
            firstPage++;
            if (boundFlag == 1) {
                accessNetParkingLoadMore(500, firstPage);
            } else if (boundFlag == 2) {
                accessNetParkingLoadMore(1000, firstPage);
            } else if (boundFlag == 3) {
                accessNetParkingLoadMore(2000, firstPage);
            } else if (boundFlag == 4) {
                accessNetParkingLoadMore(5000, firstPage);
            } else if (boundFlag == 5) {
                accessNetParkingLoadMore(10000, firstPage);
            }
        } else {
            mRefreshToilet.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (boundFlag == 1) {
            refreshAccessNetParking(500);
        } else if (boundFlag == 2) {
            refreshAccessNetParking(1000);
        } else if (boundFlag == 3) {
            refreshAccessNetParking(2000);
        } else if (boundFlag == 4) {
            refreshAccessNetParking(5000);
        } else if (boundFlag == 5) {
            refreshAccessNetParking(10000);
        }
        mRefreshToilet.finishRefresh();
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
