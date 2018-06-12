package com.qin.fragment.main.gas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.RouteShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.activity.LoginActivity;
import com.qin.adapter.recyclerview.gas.AllGasRecycleAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.collect.Collect;
import com.qin.pojo.gasstation.AllGasStation;
import com.qin.pojo.gasstation.Data;
import com.qin.pojo.gasstation.Pageinfo;
import com.qin.pojo.gasstation.Price;
import com.qin.pojo.gasstation.Result;
import com.qin.pojo.up.gasstation.UpGasstation;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Administrator on 2018/3/30 0012.
 */

public class ParkingFragment2 extends BaseFragment implements OnRefreshLoadMoreListener, INaviInfoCallback, OnGetShareUrlResultListener {

    private static final String TAG = "AllGasFragment";

    @BindView(R.id.appbar_gas)
    AppBarLayout appbarGas;
    @BindView(R.id.recyclerView_gas_all)
    RecyclerView recyclerViewGasAll;
    @BindView(R.id.refresh_gas)
    SmartRefreshLayout refreshGas;

    private String city;
    private int page = 1;
    Unbinder unbinder;
    private View mView;
    private List<Data> mDatas = new ArrayList<>();
    private double mLat;
    private double mLon;
    private AllGasRecycleAdapter mAdapter;
    private Pageinfo mPageinfo;
    private int mCurrent;
    private int mPnums;
    private ShareUrlSearch mShareUrlSearch;
    private String currentAddr;
    private Dialog mShareDialog;
    private AmapTTSController amapTTSController;
    private String mUserid;
    private String mSpUser_id;
    private Dialog mDialog;
    private LinearLayoutManager mLayoutManager;
    private Dialog mLoginDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(mActivity).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        mView = View.inflate(mActivity, R.layout.fragment_gas_all, null);
        unbinder = ButterKnife.bind(this, mView);
        initDialog(mActivity);
        initDialogLogin();
        amapTTSController = AmapTTSController.getInstance(mActivity);
        amapTTSController.init();
        return mView;
    }

    @Override
    protected void initData() {
        //正常加载显示的界面
        Log.i("viewpager", this.getClass().getName());
        mLat = mActivity.getIntent().getExtras().getDouble(ConstantValues.NEARBY_LAT);
        mLon = mActivity.getIntent().getExtras().getDouble(ConstantValues.NEARBY_LON);
        Log.i("lat", mLat + "--------" + mLon);

        //下拉刷新，上拉加载更多
        refreshGas.setOnRefreshLoadMoreListener(this);
        refreshGas.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);
        accessNet(1, 10000,3);
        mShareUrlSearch = ShareUrlSearch.newInstance();
        mShareUrlSearch.setOnGetShareUrlResultListener(this);

    }

    private void initDialog(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    private void initShareDialog(final double a, final double o, final String name) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.55f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        mShareDialog = new Dialog(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_share, null);
        LinearLayout ll_dialog_share_location = view.findViewById(R.id.ll_dialog_share_location);
        LinearLayout ll_dialog_share_route = view.findViewById(R.id.ll_dialog_share_route);
        ll_dialog_share_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareUrlSearch
                        .requestLocationShareUrl(new LocationShareURLOption()
                                .location(new com.baidu.mapapi.model.LatLng(a, o))
                                .name(name)
                                .snippet("易行"));
                mShareDialog.dismiss();
            }
        });
        ll_dialog_share_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareRoute(a, o);
                mShareDialog.dismiss();
            }
        });
        mShareDialog.setContentView(view);
        mShareDialog.setContentView(view, params);
        mShareDialog.setCanceledOnTouchOutside(true);
        mShareDialog.setCancelable(true);
        mShareDialog.show();
    }

    public void shareRoute(double a, double o) {
        PlanNode startNode = PlanNode.withLocation(new com.baidu.mapapi.model.LatLng(mLat, mLon));
        PlanNode enPlanNode = PlanNode.withLocation(new com.baidu.mapapi.model.LatLng(a, o));
        mShareUrlSearch.requestRouteShareUrl(new RouteShareURLOption().from(startNode).to(enPlanNode).routMode(RouteShareURLOption.RouteShareMode.CAR_ROUTE_SHARE_MODE));
    }

    public void accessNet(int page, int radius,int cate) {
        mDialog.show();
        if (mLat != 0 && mLon != 0) {
            UpGasstation gasstation = new UpGasstation();
            gasstation.setLat(mLat + "");
            gasstation.setLng(mLon + "");
            gasstation.setRadius(radius + "");
            gasstation.setPage_num(page + "");
            gasstation.setCate(cate);
            String json = new Gson().toJson(gasstation);
            OkGo.<String>post(ConstantValues.URL_GAS).tag(this).upJson(json).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i("lat", response.body() + "body" + mLat + "--------" + mLon);
                    //       GasStationActivity.loadinglayout.setStatus(LoadingLayout.Success);
                    parseData(response.body());
                }

                @Override
                public void onError(Response<String> response) {
                    ToastUtils.showBgResource(mActivity, "服务器出错！");
                }
            });
        }
    }

    public void accessNetCollect(String user_id, String name, String address, double lat, double lng) {
        mDialog.show();
        if (mLat != 0 && mLon != 0) {
            Gson gson = new Gson();
            Collect collect = new Collect();
            collect.setUser_id(user_id);
            collect.setName(name);
            collect.setAddress(address);
            collect.setLat(lat);
            collect.setLng(lng);
            String json = gson.toJson(collect);
            OkGo.<String>post(ConstantValues.URL_COLLECT)
                    .tag(this)
                    .upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("Collect", response.body());
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    ToastUtils.showBgResource(mActivity, "搜藏成功");
                                } else if (code.equals("2")) {
                                    ToastUtils.showBgResource(mActivity, "已收藏");
                                }
                                mDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtils.showBgResource(mActivity, "服务器出错！");
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            ToastUtils.showBgResource(mActivity, "服务器出错！");
                        }
                    });
        }
    }

    private void parseData(String json) {
        mDialog.dismiss();
        Gson gson = new Gson();
        AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
        int error_code = allGasStation.getError_code();
        if (error_code == 0) {
            Result result = allGasStation.getResult();
            mDatas = result.getData();
            Log.i(TAG, allGasStation.toString());
            mDatas.addAll(mDatas);
            mPageinfo = result.getPageinfo();
            mCurrent = mPageinfo.getCurrent();
            mPnums = mPageinfo.getPnums();
        }
        initDatas();
    }

    private void accessNetLoadMore(int page, int radius) {
        if (mLat != 0 && mLon != 0) {
            UpGasstation gasstation = new UpGasstation();
            gasstation.setLat(mLat + "");
            gasstation.setLng(mLon + "");
            gasstation.setRadius(radius + "");
            gasstation.setPage_num(page + "");
            String json = new Gson().toJson(gasstation);
            OkGo.<String>post(ConstantValues.URL_GAS).tag(this).upJson(json).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i(TAG, response.body());
                    String json = response.body();
                    Gson gson = new Gson();
                    AllGasStation allGasStation = gson.fromJson(json, AllGasStation.class);
                    List<Data> data = allGasStation.getResult().getData();
                    Log.i(TAG, allGasStation.toString());
                    mAdapter.loadMore(data);
                }
            });
        }
    }

    private void initDatas() {
        recyclerViewGasAll.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewGasAll.setLayoutManager(mLayoutManager);
        recyclerViewGasAll.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        Log.i(TAG, "走到这" + mDatas.size());
        if (mDatas.size() > 0) {
            mAdapter = new AllGasRecycleAdapter(mDatas) {
                @Override
                protected void onBbindViewHolder(ViewHolder itemView, int position) {
                    String name = mDatas.get(position).getName();
                    Log.i(TAG, name + "CityAllGasRecycleAdapter");
                    itemView.tv_gas_name.setText(name);
                    itemView.tv_gas_brandname.setText(mDatas.get(position).getBrandname());
                    itemView.tv_gas_discount.setText(mDatas.get(position).getDiscount());
                    itemView.tv_gas_exhaust.setText(mDatas.get(position).getExhaust());
                    itemView.tv_gas_location.setText(mDatas.get(position).getAddress() + "距离您大约" + mDatas.get(position).getDistance() + "米");
                    Price price = mDatas.get(position).getPrice();
                    if (price == null) {

                    } else {
                        itemView.tv_gas_e0.setText(mDatas.get(position).getPrice().getE0());
                        itemView.tv_gas_e90.setText(mDatas.get(position).getPrice().getE90());
                        itemView.tv_gas_e93.setText(mDatas.get(position).getPrice().getE93());
                        itemView.tv_gas_e97.setText(mDatas.get(position).getPrice().getE97());
                    }
                }
            };
            recyclerViewGasAll.setAdapter(mAdapter);
            mDialog.dismiss();
            mAdapter.setOnItemClickListener(new AllGasRecycleAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, AllGasRecycleAdapter.ViewName viewName, int postion) {
                    if (AllGasRecycleAdapter.ViewName.LL_GAS_COLLECTION == viewName) {
                        if (("").equals(mUserid) || mUserid == "" || mUserid == null) {
                            mLoginDialog.show();
                        } else {
                            String position = mDatas.get(postion).getPosition();
                            String[] split = position.split(",");
                            accessNetCollect(mUserid, mDatas.get(postion).getName()
                                    , mDatas.get(postion).getAddress()
                                    , Double.parseDouble(split[1])
                                    , Double.parseDouble(split[0]));
                        }
                    } else if (AllGasRecycleAdapter.ViewName.ITEM == viewName) {
                        String position = mDatas.get(postion).getPosition();
                        String[] split = position.split(",");
                        Log.i("allfragment", split[0] + "============" + split[1]);
                        //直接启动导航
                        //TODO 金纬度是字符串，带转换
                        AmapNaviPage.getInstance().showRouteActivity(mActivity
                                , new AmapNaviParams(new Poi(null, null, "")
                                        , null
                                        , new Poi(mDatas.get(postion).getName(), new LatLng(
                                        Double.parseDouble(split[1])
                                        , Double.parseDouble(split[0])), "")
                                        , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                                , ParkingFragment2.this);
                    } else if (AllGasRecycleAdapter.ViewName.LL_GAS_SHARE == viewName) {
                        String position = mDatas.get(postion).getPosition();
                        String[] split = position.split(",");
                        currentAddr = mDatas.get(postion).getName();
                        double lat = Double.parseDouble(split[1]);
                        double lon = Double.parseDouble(split[0]);
                        initShareDialog(lat, lon, mDatas.get(postion).getName());


                    } else if (AllGasRecycleAdapter.ViewName.LL_GAS_TIP == viewName) {
                        ToastUtils.showBgResource(mActivity, "提示");
                    }
                }
            });
        }
    }

    /**
     * 初始化登录对话框
     */
    private void initDialogLogin() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.65f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.45f);
        mLoginDialog = new Dialog(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_tip_login, null);
        TextView tv_login_login = view.findViewById(R.id.tv_login_login);
        TextView tv_login_cancel = view.findViewById(R.id.tv_login_cancel);
        mLoginDialog.setContentView(view);
        mLoginDialog.setContentView(view, params);
        mLoginDialog.setCanceledOnTouchOutside(true);
        mLoginDialog.setCancelable(true);

        tv_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterLoginActivity();
                mLoginDialog.dismiss();
            }
        });
        tv_login_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
            }
        });
    }

    /**
     * 跳转到登录界面
     */
    private void EnterLoginActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    @Override
    public View initView() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShareUrlSearch.destroy();
        unbinder.unbind();
    }


    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (mCurrent < mPnums) {
            mCurrent++;
            accessNetLoadMore(mCurrent, 10000);
        } else {
            ToastUtils.showBgResource(mActivity, "已加载所有数据");
        }
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        accessNetLoadMore(1, 10000);
        refreshLayout.finishRefresh();
    }

    public ReloadGasListener mReloadGasListener;

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

    //分享接口回调
    @Override
    public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {

        // 分享短串结果
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过易行与您分享一个POI点详情: " + currentAddr
                + " 链接：" + result.getUrl());
        it.setType("text/plain");
        startActivity(Intent.createChooser(it, "将路线分享到"));

    }

    @Override
    public void onGetLocationShareUrlResult(ShareUrlResult result) {

        // 分享短串结果
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过易行与您分享一个位置: " + currentAddr
                + "链接：" + result.getUrl());
        it.setType("text/plain");
        startActivity(Intent.createChooser(it, "将位置分享到"));

    }

    @Override
    public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过易行与您分享一条路线，URL "
                + " 链接：" + shareUrlResult.getUrl());
        it.setType("text/plain");
        Intent intent = Intent.createChooser(it, "将路线分享到");
        if (null == intent) {
            ToastUtils.showBgResource(mActivity, "抱歉，分享目标选择失败");
        }
        startActivity(intent);
    }

    public interface ReloadGasListener {
        public void reLoadGas();
    }

    public void setReloadGasListener(ReloadGasListener reloadGasListener) {
        this.mReloadGasListener = reloadGasListener;
    }
}
