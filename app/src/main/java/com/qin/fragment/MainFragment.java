package com.qin.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.qin.R;
import com.qin.activity.BreakAlarmActivity;
import com.qin.activity.BreakRepairActivity;
import com.qin.activity.CarControlMoreActivity;
import com.qin.activity.CarControlWebActivity;
import com.qin.activity.nearby.GasStationActivity;
import com.qin.activity.LocationActivity;
import com.qin.activity.LoginActivity;
import com.qin.activity.OwnerServiceActivity;
import com.qin.activity.nearby.ParkingActivity;
import com.qin.activity.nearby.RepairShopActivity;
import com.qin.activity.nearby.ServiceActivity;
import com.qin.activity.nearby.ToliteActivity;
import com.qin.activity.nearby.VehicleOfficeActivity;
import com.qin.activity.nearby.WashCarActivity;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.map.activity.baidu.BaiduMapSearchPoiActivity;
import com.qin.pojo.airquality.AirQuality;
import com.qin.pojo.breakrule.BreakRule;
import com.qin.pojo.carcontrol.CarControl;
import com.qin.pojo.carcontrol.Lists;
import com.qin.pojo.todaygasprice.GasPrice;
import com.qin.pojo.todaygasprice.Result;
import com.qin.pojo.weather.Today;
import com.qin.pojo.weather.Weather;
import com.qin.util.GlideImageLoader;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.qin.view.textview.RunTextViewVertical;
import com.qin.view.textview.RunTextViewVerticalMore;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class MainFragment extends Fragment {

    private static final String KEY_TITLE = "title";
    @BindView(R.id.iv_main_topbackgraound)
    ImageView ivMainTopbackgraound;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.ll_main_gas_station)
    LinearLayout llMainGasStation;
    @BindView(R.id.ll_main_parking)
    LinearLayout llMainParking;
    @BindView(R.id.ll_main_repair_shop)
    LinearLayout llMainRepairShop;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    Unbinder unbinder;
    @BindView(R.id.tv_main_location)
    TextView tvMainLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.toolbar_tempbound)
    TextView toolbarTempbound;
    @BindView(R.id.toolbar_advice)
    RunTextViewVertical toolbarAdvice;
    @BindView(R.id.tv_main_todaygasprice)
    RunTextViewVertical tvMainTodaygasprice;
    @BindView(R.id.tv_main_breakrule)
    RunTextViewVerticalMore tvMainBreakrule;
    @BindView(R.id.ll_main_breakrule)
    LinearLayout llMainBreakrule;
    @BindView(R.id.tv_main_qirquality)
    TextView tvMainQirquality;
    @BindView(R.id.tv_main_measure)
    TextView tvMainMeasure;
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
    @BindView(R.id.ll_main_breakrule_check)
    LinearLayout llMainBreakruleCheck;
    @BindView(R.id.ll_main_breakrule_happen)
    LinearLayout llMainBreakruleHappen;
    @BindView(R.id.ll_main_limitd)
    LinearLayout llMainLimitd;
    @BindView(R.id.ll_main_headingcode)
    LinearLayout llMainHeadingcode;
    @BindView(R.id.ll_main_cartype)
    LinearLayout llMainCartype;
    @BindView(R.id.ll_main_platechck)
    LinearLayout llMainPlatechck;
    @BindView(R.id.ll_main_breakdown)
    LinearLayout llMainBreakdown;
    @BindView(R.id.ll_main_carinsurance)
    LinearLayout llMainCarinsurance;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_main_breakrule_happen)
    TextView tvMainBreakruleHappen;
    @BindView(R.id.tv_breakrule)
    TextView tvBreakrule;
    @BindView(R.id.tv_breakrulehappen)
    TextView tvBreakrulehappen;
    @BindView(R.id.tv_endcodlimited)
    TextView tvEndcodlimited;
    @BindView(R.id.tv_vinanalysis)
    TextView tvVinanalysis;
    @BindView(R.id.tv_cartype)
    TextView tvCartype;
    @BindView(R.id.tv_platechenck)
    TextView tvPlatechenck;
    @BindView(R.id.tv_breakdowndtc)
    TextView tvBreakdowndtc;
    @BindView(R.id.tv_carinsurance)
    TextView tvCarinsurance;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_main_carcotrolmore)
    TextView tvMainCarcotrolmore;
    @BindView(R.id.tv_main_trafficmore)
    TextView tvMainTrafficmore;
    @BindView(R.id.banner_traffic)
    Banner bannerTraffic;

    private View mView;
    private double mLatitude;
    private double mLongitude;
    private FragmentActivity mActivity;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String mCity;
    private double mLat;
    private double mLon;
    private ArrayList<String> titleList = new ArrayList<String>();
    private static final String URL_HEAD = "http://apis.haoservice.com/weather/geo?lon=";
    private static final String URL_MID = "&lat=";
    private static final String URL_END = "8&key=a5f0c24240604416869a05667fdda0e7";
    private String mUrl;
    private static final String TODAY_GAS_PRICE = "http://api.jisuapi.com/oil/query?appkey=eade6fcaffe9929d&province=";
    private static final String BREAKRULE_LAT = "http://api.jisuapi.com/illegaladdr/coord?lat=";
    private static final String BREAKRULE_LNG = "&lng=";
    private static final String BREAKRULE_NUM = "&num=";
    private String mNum = "20";
    private static final String BREAKRULE_RANGE = "&range=";
    private String mRange = "5000";
    private static final String BREAKRULE_END = "&appkey=eade6fcaffe9929d";
    private static final String AIR_QUALITY = "http://api.jisuapi.com/aqi/query?appkey=eade6fcaffe9929d&city=";
    private static final String CARCONTROL_URL_HEAD = "http://api.jisuapi.com/news/search?keyword=";
    private static final String CARCONTROL_URL_END = "汽车&appkey=eade6fcaffe9929d";
    private static final String TRAFFIC_URL_HEAD = "http://api.jisuapi.com/news/search?keyword=";
    private String allCity = "福州";
    private static final String TRAFFIC_URL_END = "交通&appkey=eade6fcaffe9929d";
    private ArrayList<String> mGasPriceList = new ArrayList<>();
    List<View> views = new ArrayList<>();
    private String province;
    private List<com.qin.pojo.breakrule.Result> mResult;
    private String airQualityCity;
    private Dialog mDialog;
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> imagesTraffic = new ArrayList<>();
    private List<String> titlesTraffic = new ArrayList<>();
    private ArrayList<Lists> mCarResultList;
    private ArrayList<Lists> mTrafficResultList;
    private String mUserid;
    private String mSpUser_id;
    private Dialog mLoginDialog;

    /**
     * 生成mainfragment实例并传值
     *
     * @param title
     * @return
     */
    public static MainFragment newInstance(String title) {
        MainFragment homeFragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        homeFragment.setArguments(args);
        return (homeFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(getActivity()).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mView);
        initDialog();
        initDialogLogin();
        mActivity = getActivity();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);
        tvMainLocation.setSelected(true);
        initLocation();
        fillFab();
        loadTopBackground();
        toolbarTempbound.setSelected(true);
        tvMainMeasure.setSelected(true);
        mDialog.show();
        startLocation();
        return mView;
    }

    /**
     * 获取空气质量信息
     */
    private void accessNetAirQuality() {
        String url = AIR_QUALITY + airQualityCity;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("AirQuality", response.body());
                parseDataAirQuality(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("AirQuality", "空气质量异常");
                ToastUtils.showBgResource(mActivity, "服务器异常");
            }
        });
    }

    /**
     * 解析json空气质量数据
     *
     * @param json
     */
    private void parseDataAirQuality(String json) {
        Gson gson = new Gson();
        AirQuality airQuality = gson.fromJson(json, AirQuality.class);
        String quality = airQuality.getResult().getQuality();
        String measure = airQuality.getResult().getAqiinfo().getMeasure();
        tvMainQirquality.setText("空气质量 : " + quality);
        tvMainMeasure.setText(measure);
    }

    /**
     * 获取违规违章数据（极速数据）
     */
    private void accessNetBreakRule() {
        Log.i("BreakRule", "accessNetBreakRule");
        String url = BREAKRULE_LAT + mLatitude + BREAKRULE_LNG + mLongitude + BREAKRULE_NUM + mNum
                + BREAKRULE_RANGE + mRange + BREAKRULE_END;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("BreakRule", response.body());
                parseDataBreakRule(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("BreakRule", "违章高发地极速数据异常");
                ToastUtils.showBgResource(mActivity, "服务器异常");
            }
        });
    }

    /**
     * 解析json违规违章数据
     *
     * @param json
     */
    private void parseDataBreakRule(String json) {
        Gson gson = new Gson();
        BreakRule breakRule = gson.fromJson(json, BreakRule.class);
        mResult = breakRule.getResult();
        setUPMarqueeView(views, mResult.size());
        tvMainBreakrule.setViews(views);
    }

    /**
     * 获取今日油价数据
     */
    private void accessNetGASPrice() {
        mDialog.dismiss();
        String url = TODAY_GAS_PRICE + province;
        OkGo.<String>post(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("GASPrice", response.body());
                parseDataGASPrice(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                ToastUtils.showBgResource(mActivity, "服务器异常");
            }
        });
    }

    /**
     * 解析今日油价数据
     *
     * @param json
     */
    private void parseDataGASPrice(String json) {
        isGasPrice = false;
        Gson gson = new Gson();
        GasPrice gasPrice = gson.fromJson(json, GasPrice.class);
        Result result = gasPrice.getResult();
        mGasPriceList.add("汽油89 : " + result.getOil89() + "/升");
        mGasPriceList.add("汽油90 : " + result.getOil90() + "/升");
        mGasPriceList.add("汽油92 : " + result.getOil92() + "/升");
        mGasPriceList.add("汽油93 : " + result.getOil93() + "/升");
        mGasPriceList.add("汽油95 : " + result.getOil95() + "/升");
        mGasPriceList.add("汽油97 : " + result.getOil97() + "/升");
        mGasPriceList.add("汽油98 : " + result.getOil98() + "/升");
        mGasPriceList.add("柴油0 : " + result.getOil0() + "/升");
        initTodayGasPrice();
    }

    /**
     * 初始化今日油价自动上浮控件
     */
    private void initTodayGasPrice() {
        tvMainTodaygasprice.setTextList(mGasPriceList);
        tvMainTodaygasprice.setText(12, 5, R.color.deepgray);//设置属性
        tvMainTodaygasprice.setTextStillTime(2000);//设置停留时长间隔
        tvMainTodaygasprice.setAnimTime(500);//设置进入和退出的时间间隔
        tvMainTodaygasprice.setOnItemClickListener(new RunTextViewVertical.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtils.showBgResource(mActivity, "我共有+" + 1000 + "次" + "来自极速数据");
            }
        });
        tvMainTodaygasprice.startAutoScroll();
    }

    /**
     * 设值并初始化违规违章上浮控件
     *
     * @param views
     * @param size
     */
    private void setUPMarqueeView(List<View> views, int size) {
        for (int i = 0; i < size; i++) {
            final LinearLayout moreView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_view, null);
            //初始化布局的控件
            TextView tv1 = moreView.findViewById(R.id.tv1);
            TextView tv2 = moreView.findViewById(R.id.tv2);
            TextView tv_distance = moreView.findViewById(R.id.tv_distance);
            TextView tv_num = moreView.findViewById(R.id.tv_num);
            moreView.findViewById(R.id.ll_main_breakrule).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    ToastUtils.showBgResource(getActivity(), "违章");
                    Intent intent = new Intent();
                    if (mLatitude != 0 && mLongitude != 0) {
                        setStartFragmentParams1(mActivity, intent, 2, mLatitude, mLongitude, OwnerServiceActivity.class);
                    } else {
                        setStartFragmentParams1(mActivity, intent, 2, mLat, mLon, OwnerServiceActivity.class);
                    }
                }
            });
            //进行对控件赋值
            tv1.setText(mResult.get(i).getAddress());
            tv2.setText(mResult.get(i).getContent());
            String s = mResult.get(i).getDistance();
            String distance = s.substring(0, 4);
            tv_distance.setText(distance + "m");
            tv_num.setText(mResult.get(i).getNum() + "(违章)");
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }

    /**
     * 获取紫外线强渡和洗车指数数据（haoservice）
     */
    public void accessNet() {
        if (mLatitude != 0 && mLongitude != 0) {
            mUrl = URL_HEAD + mLongitude + URL_MID + mLatitude + URL_END;
            OkGo.<String>post(mUrl).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.i("lat", response.body() + "body" + mLatitude + "----onSuccess----" + mLatitude);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String error_code = jsonObject.getString("error_code");
                        if (error_code.equals("0")) {
                            parseData(response.body());
                        } else {
                            ToastUtils.showBgResource(mActivity, jsonObject.getString("第三方数据异常"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Response<String> response) {
                    ToastUtils.showBgResource(mActivity, "服务器异常");
                }
            });
        }
    }

    /**
     * 解析json洗车紫外线数据
     *
     * @param json
     */
    private void parseData(String json) {
        Gson gson = new Gson();
        Weather weather = gson.fromJson(json, Weather.class);
        Today today = weather.getResult().getToday();
        titleList.add("洗车指数 : " + today.getWash_index());
        titleList.add("紫外线强度 : " + today.getUv_index());
        toolbarTempbound.setText(today.getDressing_advice());
        initText();
    }

    /**
     * 获取汽车控数据
     */
    private void accessNetCarControl() {
        String url = CARCONTROL_URL_HEAD + allCity + CARCONTROL_URL_END;
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("CarControl", response.body());
                try {
                    parseDataCarControl(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("CarControl", "极速数据异常");
                ToastUtils.showBgResource(mActivity, "服务器异常");
            }
        });
    }

    /**
     * 解析汽车控json数据
     */
    private void accessNetTrafficNews() {
        String url = TRAFFIC_URL_HEAD + allCity + TRAFFIC_URL_END;
        OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("TrafficNews", response.body());
                try {
                    parseDataTrafficNews(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response<String> response) {
                Log.i("TrafficNews", "极速数据异常");
                ToastUtils.showBgResource(mActivity, "服务器异常");
            }
        });
    }

    /**
     * 获取汽车新闻数据
     *
     * @param body
     */
    private void parseDataTrafficNews(String body) {
        Gson gson = new Gson();
        CarControl carControl = gson.fromJson(body, CarControl.class);
        com.qin.pojo.carcontrol.Result result = carControl.getResult();
        mTrafficResultList = (ArrayList<Lists>) result.getList();
        //TODO 使用piscio加载图片+viewpager
        imagesTraffic.add(mTrafficResultList.get(0).getPic());
        imagesTraffic.add(mTrafficResultList.get(1).getPic());
        imagesTraffic.add(mTrafficResultList.get(2).getPic());
        imagesTraffic.add(mTrafficResultList.get(3).getPic());
        imagesTraffic.add(mTrafficResultList.get(4).getPic());
        titlesTraffic.add(mTrafficResultList.get(0).getTitle());
        titlesTraffic.add(mTrafficResultList.get(1).getTitle());
        titlesTraffic.add(mTrafficResultList.get(2).getTitle());
        titlesTraffic.add(mTrafficResultList.get(3).getTitle());
        titlesTraffic.add(mTrafficResultList.get(4).getTitle());
        initBannerTraffic();
    }

    /**
     * 解析json汽车新闻数据
     *
     * @param body
     */
    private void parseDataCarControl(String body) {
        Gson gson = new Gson();
        CarControl carControl = gson.fromJson(body, CarControl.class);
        com.qin.pojo.carcontrol.Result result = carControl.getResult();
        mCarResultList = (ArrayList<Lists>) result.getList();
        //TODO 使用piscio加载图片+viewpager
        images.add(mCarResultList.get(0).getPic());
        images.add(mCarResultList.get(1).getPic());
        images.add(mCarResultList.get(2).getPic());
        images.add(mCarResultList.get(3).getPic());
        images.add(mCarResultList.get(4).getPic());
        titles.add(mCarResultList.get(0).getTitle());
        titles.add(mCarResultList.get(1).getTitle());
        titles.add(mCarResultList.get(2).getTitle());
        titles.add(mCarResultList.get(3).getTitle());
        titles.add(mCarResultList.get(4).getTitle());
        initBanner();
    }

    /**
     * 初始化交通轮播控件
     */
    private void initBannerTraffic() {
        bannerTraffic.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        bannerTraffic.setImageLoader(new GlideImageLoader());
        bannerTraffic.setImages(imagesTraffic);
        bannerTraffic.setBannerAnimation(Transformer.Accordion);
        bannerTraffic.setBannerTitles(titlesTraffic);
        bannerTraffic.isAutoPlay(true);
        bannerTraffic.setDelayTime(3000);
        bannerTraffic.setIndicatorGravity(BannerConfig.CENTER);
        bannerTraffic.start();
        bannerTraffic.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("URL", mTrafficResultList.get(position).getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mActivity, CarControlWebActivity.class);
                startActivity(intent);
            }
        });
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        refreshLayout.finishRefresh();
    }

    /**
     * 初始化汽车控轮播控件
     */
    private void initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerTitles(titles);
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("URL", mCarResultList.get(position).getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mActivity, CarControlWebActivity.class);
                startActivity(intent);
            }
        });
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 设置洗车指数，紫外线控件
     */
    private void initText() {
        toolbarAdvice.setTextList(titleList);
        toolbarAdvice.setText(14, 10, R.color.colorWrite);//设置属性
        toolbarAdvice.setTextStillTime(3000);//设置停留时长间隔
        toolbarAdvice.setAnimTime(500);//设置进入和退出的时间间隔
        toolbarAdvice.setOnItemClickListener(new RunTextViewVertical.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        toolbarAdvice.startAutoScroll();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //下拉刷新触发的事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                startLocation();
                images.clear();
                imagesTraffic.clear();
                titles.clear();
                titlesTraffic.clear();
//              accessNet();
//              accessNetGASPrice();
                accessNetCarControl();
                accessNetTrafficNews();
            }
        });
        refreshLayout.setPrimaryColorsId(R.color.colorGreen, android.R.color.white);
    }

    /**
     * 加载主页面顶部背景
     */
    private void loadTopBackground() {
        final ImageView imageView = mView.findViewById(R.id.iv_main_topbackgraound);
        imageView.setImageResource(R.drawable.mainfragment_top_bg_tran);
    }

    /**
     * 初始化悬浮控件
     */
    private void fillFab() {
        final FloatingActionButton fab = mView.findViewById(R.id.floating_action_button);
        fab.setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */

    @OnClick({R.id.ll_main_breakrule, R.id.ll_location, R.id.ll_main_gas_station, R.id.ll_main_parking, R.id.ll_main_repair_shop, R.id.floating_action_button
            , R.id.ll_main_repair_wash, R.id.ll_main_service, R.id.ll_main_vehicleoffice, R.id.ll_main_toilet, R.id.ll_main_maintain, R.id.ll_main_breakrule_check
            , R.id.ll_main_breakrule_happen, R.id.ll_main_limitd, R.id.ll_main_headingcode, R.id.ll_main_cartype, R.id.tv_main_trafficmore
            , R.id.ll_main_platechck, R.id.ll_main_breakdown, R.id.ll_main_carinsurance, R.id.tv_main_carcotrolmore})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        mCity = SPUtils.getInstance(mActivity).getString(ConstantValues.MAIN_LOCATION_CITY, "获取失败");
        mLat = Double.parseDouble(SPUtils.getInstance(mActivity).getString(ConstantValues.MAIN_LOCATION_LAT, "0"));
        mLon = Double.parseDouble(SPUtils.getInstance(mActivity).getString(ConstantValues.MAIN_LOCATION_LON, "0"));
        switch (view.getId()) {
            /**
             * 定位
             */
            case R.id.ll_location:
                intent.putExtra(ConstantValues.LOCAATION, "定位");
                intent.setClass(mActivity, LocationActivity.class);
                intent.putExtra("currentLocationcity", mCity);
                intent.putExtra("currentLocation", tvMainLocation.getText().toString().trim());
                startActivityForResult(intent, 1, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, tvMainLocation, "currentpostion").toBundle());
                break;
            /**
             * 附近加油站
             */
            case R.id.ll_main_gas_station:
                if (mLatitude != 0 && mLongitude != 0) {
                    GasStationActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    GasStationActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近停车场
             */
            case R.id.ll_main_parking:
                if (mLatitude != 0 && mLongitude != 0) {
                    ParkingActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    ParkingActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近维修店
             */
            case R.id.ll_main_repair_shop:
                if (mLatitude != 0 && mLongitude != 0) {
                    RepairShopActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    RepairShopActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 悬浮按钮
             */
            case R.id.floating_action_button:
//                intent.setClass(mActivity, UnityPlayerActivity.class);
//                startActivity(intent);
                break;
            /**
             * 附近违规违章
             */
            case R.id.tv_main_breakrule:
//                ToastUtils.showBgResource(getActivity(), "违章");
//                if (mLatitude != 0 && mLongitude != 0) {
//                    setStartFragmentParams(mActivity, intent, 2, mLatitude, mLongitude, OwnerServiceActivity.class,tvBreakrulehappen);
//                } else {
//                    setStartFragmentParams(mActivity, intent, 2, mLat, mLon, OwnerServiceActivity.class,tvBreakrulehappen);
//                }
                break;
            /**
             * 附近洗车
             */
            case R.id.ll_main_repair_wash:
                if (mLatitude != 0 && mLongitude != 0) {
                    WashCarActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    WashCarActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近服务区
             */
            case R.id.ll_main_service:
                if (mLatitude != 0 && mLongitude != 0) {
                    ServiceActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    ServiceActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近车管所
             */
            case R.id.ll_main_vehicleoffice:
                if (mLatitude != 0 && mLongitude != 0) {
                    VehicleOfficeActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    VehicleOfficeActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近公共厕所
             */
            case R.id.ll_main_toilet:
                if (mLatitude != 0 && mLongitude != 0) {
                    ToliteActivity.startActivityWithParams(mActivity, intent, 1, mLatitude, mLongitude);
                } else {
                    ToliteActivity.startActivityWithParams(mActivity, intent, 1, mLat, mLon);
                }
                break;
            /**
             * 附近自定义查看更多
             */
            case R.id.ll_main_maintain:
                intent.putExtra("latitude", mLatitude);
                intent.putExtra("longitude", mLongitude);
                intent.setClass(mActivity, BaiduMapSearchPoiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            /**
             * 违规违章查询
             */
            case R.id.ll_main_breakrule_check:
                setStartFragment1(mActivity, intent, 1, OwnerServiceActivity.class, tvBreakrule);
                break;
            /**
             * 违章高发地查询
             */
            case R.id.ll_main_breakrule_happen:
                if (mLatitude != 0 && mLongitude != 0) {
                    setStartFragmentParams(mActivity, intent, 2, mLatitude, mLongitude, OwnerServiceActivity.class, tvBreakrulehappen);
                } else {
                    setStartFragmentParams(mActivity, intent, 2, mLat, mLon, OwnerServiceActivity.class, tvBreakrulehappen);
                }
                break;
            /**
             * 汽车尾号限行查询
             */
            case R.id.ll_main_limitd:
                setStartFragment1(mActivity, intent, 3, OwnerServiceActivity.class, tvEndcodlimited);
                break;
            /**
             * 汽车vin解析
             */
            case R.id.ll_main_headingcode:
                setStartFragment1(mActivity, intent, 4, OwnerServiceActivity.class, tvVinanalysis);
                break;
            /**
             * 汽车类型大全
             */
            case R.id.ll_main_cartype:
                setStartFragment1(mActivity, intent, 5, OwnerServiceActivity.class, tvCartype);
                break;
            /**
             * 故障报修
             */
            case R.id.ll_main_platechck:
                if (("").equals(mUserid) || mUserid == "" || mUserid == null) {
                    mLoginDialog.show();
                } else {
                    //    setStartFragment1(mActivity, intent, 8, OwnerServiceActivity.class, tvCarinsurance);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(mActivity, BreakRepairActivity.class);
                    startActivity(intent);
                }
                break;
            /**
             * 汽车故障码查询
             */
            case R.id.ll_main_breakdown:
                setStartFragment1(mActivity, intent, 7, OwnerServiceActivity.class, tvBreakdowndtc);
                break;
            /**
             * 事故报警
             */
            case R.id.ll_main_carinsurance:
                if (("").equals(mUserid) || mUserid == "" || mUserid == null) {
                    mLoginDialog.show();
                } else {
                //    setStartFragment1(mActivity, intent, 8, OwnerServiceActivity.class, tvCarinsurance);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(mActivity, BreakAlarmActivity.class);
                    startActivity(intent);
                }
                break;
            /**
             * 汽车资讯查看更多
             */
            case R.id.tv_main_carcotrolmore:
                intent.putParcelableArrayListExtra("CARCONTROL", mCarResultList);
                intent.putExtra("title", "汽车资讯");
                intent.setClass(mActivity, CarControlMoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            /**
             * 交通资讯查看更多
             */
            case R.id.tv_main_trafficmore:
                intent.putParcelableArrayListExtra("CARCONTROL", mTrafficResultList);
                intent.putExtra("title", "交通快讯");
                intent.setClass(mActivity, CarControlMoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化正在加载中对话框
     */
    private void initDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(getActivity()), ScreenUtils.getWindowHeight(getActivity()));
        params.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        params.height = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.4f);
        mDialog = new Dialog(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    /**
     * 初始化登录对话框
     */
    private void initDialogLogin() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(getActivity()), ScreenUtils.getWindowHeight(getActivity()));
        params.width = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.65f);
        params.height = (int) (getActivity().getWindowManager().getDefaultDisplay().getWidth() * 0.45f);
        mLoginDialog = new Dialog(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_tip_login, null);
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
        intent.setClass(mActivity, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
    }

    /**
     * 启动fragment
     *
     * @param context
     * @param intent
     * @param number
     * @param clzss
     */
    @NonNull
    public void setStartFragment(Context context, Intent intent, int number, Class clzss) {
        intent = new Intent();
        intent.putExtra(ConstantValues.OWNERNUMBER, number);
        intent.setClass(context, clzss);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param intent
     * @param number
     * @param clzss
     * @param textView
     */
    @NonNull
    public void setStartFragment1(Context context, Intent intent, int number, Class clzss, TextView textView) {
        intent = new Intent();
        intent.putExtra(ConstantValues.OWNERNUMBER, number);
        intent.setClass(context, clzss);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param intent
     * @param number
     * @param lat
     * @param lon
     * @param clzss
     * @param textView
     */
    @NonNull
    public void setStartFragmentParams(Context context, Intent intent, int number, double lat, double lon, Class clzss, TextView textView) {
        intent = new Intent();
        intent.putExtra(ConstantValues.OWNERNUMBER, number);
        intent.putExtra(ConstantValues.MAIN_LOCATION_LAT, lat);
        intent.putExtra(ConstantValues.MAIN_LOCATION_LON, lon);
        intent.setClass(context, clzss);
        context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, textView, "main").toBundle());
    }

    /**
     * @param context
     * @param intent
     * @param number
     * @param lat
     * @param lon
     * @param clzss
     */
    @NonNull
    public void setStartFragmentParams1(Context context, Intent intent, int number, double lat, double lon, Class clzss) {
        intent = new Intent();
        intent.putExtra(ConstantValues.OWNERNUMBER, number);
        intent.putExtra(ConstantValues.MAIN_LOCATION_LAT, lat);
        intent.putExtra(ConstantValues.MAIN_LOCATION_LON, lon);
        intent.setClass(context, clzss);
        context.startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
        //    mLocationService.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String editTextName = data.getExtras().getString("editTextName", "定位失败");
                if (!"".equals(editTextName)) {
                    tvMainLocation.setText(editTextName);
                }
            }
        }
    }

    /**
     * 初始化定位参数
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(mActivity.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 开启定位
     */
    private void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 初始化定位参数
     *
     * @return
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private boolean isGasPrice = true;

    /**
     * 定位监听类
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                }
                sb.append("****************").append("\n");
                //解析定位结果，
                String result = sb.toString();
                Log.i("location", result);
                allCity = location.getCity().substring(0, 2);
                tvMainLocation.setText(location.getAddress());
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                province = location.getProvince().substring(0, 2);
                airQualityCity = location.getCity();
                Log.i("province", province);
                //TODO 访问第三方数据
                       accessNetBreakRule();
                       accessNetAirQuality();
                if (isGasPrice) {
                               accessNetGASPrice();
                }
              accessNet();
                       accessNetCarControl();
                      accessNetTrafficNews();
                SPUtils.getInstance(mActivity).putString(ConstantValues.MAIN_LOCATION_LAT, location.getLatitude() + "", true);
                SPUtils.getInstance(mActivity).putString(ConstantValues.MAIN_LOCATION_LON, location.getLongitude() + "", true);
                SPUtils.getInstance(mActivity).putString(ConstantValues.MAIN_LOCATION_CITY, location.getCity(), true);
                tvMainLocation.setTextColor(getResources().getColor(R.color.colorWrite));

            } else {
                tvMainLocation.setText("定位失败");
                tvMainLocation.setTextColor(Color.RED);
            }
        }
    };
}
