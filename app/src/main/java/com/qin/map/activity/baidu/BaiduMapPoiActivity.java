package com.qin.map.activity.baidu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviTheme;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.activity.LoginActivity;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.map.baiduoverlay.PoiOverlay;
import com.qin.map.util.AmapTTSController;
import com.qin.pojo.collect.Collect;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BaiduMapPoiActivity extends Activity implements SensorEventListener, OnGetPoiSearchResultListener, INaviInfoCallback, OnGetShareUrlResultListener {

    // 定位相关
    LocationClient mLocClient;

    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.tv_baidumap_searchpoi)
    TextView tvBaidumapSearchpoi;
    @BindView(R.id.search_bar_layout)
    LinearLayout searchBarLayout;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.iv_poi_photo)
    ImageView ivPoiPhoto;
    @BindView(R.id.tv_poi_name)
    TextView tvPoiName;
    @BindView(R.id.tv_poi_addressandis)
    TextView tvPoiAddressandis;
    @BindView(R.id.tv_poi_shophours)
    TextView tvPoiShophours;
    @BindView(R.id.tv_poi_price)
    TextView tvPoiPrice;
    @BindView(R.id.rat_poi_overallrating)
    MaterialRatingBar ratPoiOverallrating;
    @BindView(R.id.tv_poi_overallrating)
    TextView tvPoiOverallrating;
    @BindView(R.id.ll_poi_panorama)
    LinearLayout llPoiPanorama;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.iv_poi_collection)
    ImageView ivPoiCollection;
    @BindView(R.id.ll_poi_collection)
    LinearLayout llPoiCollection;
    @BindView(R.id.ll_poi_share)
    LinearLayout llPoiShare;
    @BindView(R.id.iv_poi_go)
    ImageView ivPoiGo;
    @BindView(R.id.tv_poi_go)
    TextView tvPoiGo;
    @BindView(R.id.ll_poi_go)
    LinearLayout llPoiGo;
    @BindView(R.id.iv_poi_phone)
    ImageView ivPoiPhone;
    @BindView(R.id.ll_poi_moreinfo)
    LinearLayout llPoiMoreinfo;
    @BindView(R.id.rat_poi_environmentrating)
    MaterialRatingBar ratPoiEnvironmentrating;
    @BindView(R.id.tv_poi_environmentrating)
    TextView tvPoiEnvironmentrating;
    @BindView(R.id.rat_poi_servicerating)
    MaterialRatingBar ratPoiServicerating;
    @BindView(R.id.tv_poi_servicerating)
    TextView tvPoiServicerating;
    @BindView(R.id.rat_poi_hygienerating)
    MaterialRatingBar ratPoiHygienerating;
    @BindView(R.id.tv_poi_hygienerating)
    TextView tvPoiHygienerating;
    @BindView(R.id.rat_poi_facilityrating)
    MaterialRatingBar ratPoiFacilityrating;
    @BindView(R.id.tv_poi_facilityrating)
    TextView tvPoiFacilityrating;
    @BindView(R.id.rat_poi_tasterating)
    MaterialRatingBar ratPoiTasterating;
    @BindView(R.id.tv_poi_tasterating)
    TextView tvPoiTasterating;
    @BindView(R.id.tv_poi_comment)
    TextView tvPoiComment;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.ll_poi_write)
    LinearLayout llPoiWrite;
    @BindView(R.id.tv_poi_distance)
    TextView tvPoiDistance;
    @BindView(R.id.tv_poi_phone)
    TextView tvPoiPhone;
    @BindView(R.id.iv_baidumap_back)
    ImageView mIvBaidumapBack;
    @BindView(R.id.tv_baidumap_keyword)
    TextView mTvBaidumapKeyword;
    @BindView(R.id.button2)
    Button mButton2;
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private PoiSearch mPoiSearch;
    private LatLng mCenter;
    private double lat;
    private double lon;
    private AmapTTSController amapTTSController;
    private String startpoi;
    private String endpoiName;
    private MyLocationListenner mListener;
    private String mString;
    private ShareUrlSearch mShareUrlSearch;
    private String currentAddr;
    private String mName;
    private InfoWindow mInfoWindow;
    private boolean isSearch = true;
    private String mNameSearch;
    private double mLatitudeSearch;
    private double mLongitudeSearch;
    private Dialog mDialog;
    private String mUserid;
    private String mSpUser_id;
    private String mAddress;
    private String mEndAddress;
    private Dialog mLoginDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap_searchmappoi);
        ButterKnife.bind(this);
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();
        initDialog();
        initDialogLogin();
        //      slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(this).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }
        tvPoiName.setSelected(true);
        tvPoiPrice.setSelected(true);
        tvPoiShophours.setSelected(true);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;
        button1.setBackgroundResource(R.mipmap.location);
//        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mListener = new MyLocationListenner();
        mLocClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //     option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mLatitudeSearch = getIntent().getExtras().getDouble("latitude");
        mLongitudeSearch = getIntent().getExtras().getDouble("longitude");
        String keyword = getIntent().getExtras().getString("keyword");
        mNameSearch = getIntent().getExtras().getString("name");
        mAddress = getIntent().getExtras().getString("address");
        int distance = (int) getIntent().getExtras().getFloat("distance");

        tvPoiName.setText(mNameSearch);
        tvPoiAddressandis.setText(mAddress);
        tvPoiDistance.setText(distance + "米");

//        MapStatus.Builder builder = new MapStatus.Builder();
//        final LatLng latlngSearch = new LatLng(mLatitudeSearch, mLongitudeSearch);
//        builder.target(latlngSearch).zoom(18.0f);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mShareUrlSearch = ShareUrlSearch.newInstance();
        mShareUrlSearch.setOnGetShareUrlResultListener(this);

        LatLng latLng = getIntent().getExtras().getParcelable("location");
        Log.i("latLng", latLng + "");
        mString = getIntent().getExtras().getString("keyword", "美食");

//        double maplat = getIntent().getExtras().getDouble("maplat", 0);
//        double maplng = getIntent().getExtras().getDouble("maplng", 0);
//        mCenter = new LatLng(maplat,maplng);
        mTvBaidumapKeyword.setText(mString);
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.maker);
//        MarkerOptions maker = new MarkerOptions().position(latlngSearch).icon(bitmapDescriptor).draggable(false);
//        maker.animateType(MarkerOptions.MarkerAnimateType.drop);
//        mBaiduMap.addOverlay(maker);
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                View popview = View.inflate(BaiduMapPoiActivity.this, R.layout.pop_mappoi_maker, null);
//                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(popview), latlngSearch, -47, new InfoWindow.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick() {
//
//                    }
//                });
//                TextView tv_mappoi_name = popview.findViewById(R.id.tv_mappoi_name);
//                TextView tv_mappoi_address = popview.findViewById(R.id.tv_mappoi_address);
//                tv_mappoi_name.setText("sdsd");
//                tv_mappoi_address.setText("dsdsd");
//                popview.findViewById(R.id.iv_mappoi_go).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
//                                , new AmapNaviParams(new Poi("", null, "")
//                                        , null
//                                        , new Poi(endpoiName, new com.amap.api.maps.model.LatLng(mLatitudeSearch, mLongitudeSearch), "")
//                                        , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
//                                , BaiduMapPoiActivity.this);
//                    }
//                });
//                mBaiduMap.showInfoWindow(mInfoWindow);
//                return true;
//            }
//        });
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BaiduMapPoiActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            StringBuilder sb = new StringBuilder(256);
            sb.append("\ngetCurrentPageCapacity-------");
            sb.append(result.getCurrentPageCapacity());
            sb.append("\ngetTotalPageNum-------");
            sb.append(result.getTotalPageNum());
            sb.append("\ngetTotalPoiNum-------");
            sb.append(result.getTotalPoiNum());
            List<PoiAddrInfo> allAddr = result.getAllAddr();
            if (allAddr != null && allAddr.size() > 0) {
                sb.append("\nallAddr.get(0).name-------");
                sb.append(allAddr.get(0).name);
                sb.append("\nallAddr.get(0).address-------");
                sb.append(allAddr.get(0).address);
            }
            sb.append("\n\n\n");
            List<PoiInfo> allPoi = result.getAllPoi();
            if (allPoi != null && allPoi.size() > 0) {
                for (int i = 0; i < allPoi.size(); i++) {
                    PoiInfo poiInfo = allPoi.get(i);
                    sb.append("\naddress-------");
                    sb.append(poiInfo.address);
                    sb.append("\ncity-------");
                    sb.append(poiInfo.city);
                    sb.append("\nhasCaterDetails-------");
                    sb.append(poiInfo.hasCaterDetails);
                    sb.append("\nisPano-------");
                    sb.append(poiInfo.isPano);
                    sb.append("\nname-------");
                    sb.append(poiInfo.name);
                    sb.append("\nphoneNum-------");
                    sb.append(poiInfo.phoneNum);
                    sb.append("\ntype-------");
                    sb.append(poiInfo.type);
                    sb.append("\nuid-------");
                    sb.append(poiInfo.uid);
                }
            }
            sb.append("\n\n\n");
            Log.i("poi", sb.toString());

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(BaiduMapPoiActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtils.showBgResource(BaiduMapPoiActivity.this, "抱歉，未找到结果");
        } else {
            //  ToastUtils.showBgResource(BaiduMapPoiActivity.this, result.getName() + ": " + result.getAddress());

            lat = result.getLocation().latitude;
            lon = result.getLocation().longitude;
            com.amap.api.maps.model.LatLng poilocation = new com.amap.api.maps.model.LatLng(result.getLocation().latitude, result.getLocation().longitude);
            com.amap.api.maps.model.LatLng currentLocation = new com.amap.api.maps.model.LatLng(mCurrentLat, mCurrentLon);
            int distance = (int) AMapUtils.calculateLineDistance(poilocation, currentLocation);

            mName = result.getName();
            tvPoiName.setText(mName);
            tvPoiAddressandis.setText(result.getAddress());
            tvPoiDistance.setText(String.valueOf(distance) + "米");
            tvPoiComment.setText(String.valueOf(result.getCommentNum()));
            tvPoiOverallrating.setText(result.getOverallRating() + "分");
            ratPoiOverallrating.setProgress((int) result.getOverallRating() * 2);
            tvPoiEnvironmentrating.setText(result.getEnvironmentRating() + "分");
            ratPoiEnvironmentrating.setProgress((int) result.getEnvironmentRating() * 2);
            tvPoiFacilityrating.setText(result.getFacilityRating() + "分");
            ratPoiFacilityrating.setProgress((int) result.getFacilityRating() * 2);
            tvPoiHygienerating.setText(result.getHygieneRating() + "分");
            ratPoiHygienerating.setProgress((int) result.getHygieneRating() * 2);
            tvPoiServicerating.setText(result.getServiceRating() + "分");
            ratPoiServicerating.setProgress((int) result.getServiceRating() * 2);
            tvPoiTasterating.setText(result.getTasteRating() + "分");
            ratPoiTasterating.setProgress((int) result.getTasteRating() * 2);
            double resultPrice = result.getPrice();
            if (resultPrice == 0) {
                tvPoiPrice.setText("暂无");
            } else {
                tvPoiPrice.setText(String.valueOf(resultPrice));
            }
            String shopHours = result.getShopHours();
            if ("".equals(shopHours) || "" == shopHours) {
                tvPoiShophours.setText("暂无");
            } else {
                tvPoiShophours.setText(result.getShopHours() + "");
            }
            String telephone = result.getTelephone();
            if ("".equals(telephone) || "" == telephone) {
                tvPoiPhone.setText("暂无");
            } else {
                tvPoiPhone.setText(telephone);
            }

            endpoiName = result.getName();
            mEndAddress = result.getAddress();

            StringBuilder sb = new StringBuilder(256);
            sb.append("\ngetAddress-------");
            sb.append(result.getAddress());
            sb.append("\ndescribeContents-------");
            sb.append(result.describeContents());
            sb.append("\ngetCheckinNum-------");
            sb.append(result.getCheckinNum());
            sb.append("\ngetCommentNum-------");
            sb.append(result.getCommentNum());
            sb.append("\ngetDetailUrl-------");
            sb.append(result.getDetailUrl());
            sb.append("\ngetEnvironmentRating-------");
            sb.append(result.getEnvironmentRating());
            sb.append("\ngetFacilityRating-------");
            sb.append(result.getFacilityRating());
            sb.append("\ngetFavoriteNum-------");
            sb.append(result.getFavoriteNum());
            sb.append("\ngetGrouponNum-------");
            sb.append(result.getGrouponNum());
            sb.append("\ngetHygieneRating-------");
            sb.append(result.getHygieneRating());
            sb.append("\ngetImageNum-------");
            sb.append(result.getImageNum());
            sb.append("\ngetLocation-------");
            sb.append(result.getLocation());
            sb.append("\ngetName-------");
            sb.append(result.getName());
            sb.append("\ngetOverallRating-------");
            sb.append(result.getOverallRating());
            sb.append("\ngetPrice-------");
            sb.append(result.getPrice());
            sb.append("\ngetServiceRating-------");
            sb.append(result.getServiceRating());
            sb.append("\ngetShopHours-------");
            sb.append(result.getShopHours());
            sb.append("\ngetTag-------");
            sb.append(result.getTag());
            sb.append("\ngetTasteRating-------");
            sb.append(result.getTasteRating());
            sb.append("\ngetTechnologyRating-------");
            sb.append(result.getTechnologyRating());
            sb.append("\ngetTasteRating-------");
            sb.append(result.getTasteRating());
            sb.append("\ngetTelephone-------");
            sb.append(result.getTelephone());
            sb.append("\ngetType-------");
            sb.append(result.getType());
            sb.append("\ngetUid-------");
            sb.append(result.getUid());
            Log.i("poidetail1", sb.toString());

            //TODO  geipoi详情控件设置值
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @OnClick({R.id.button2,R.id.iv_baidumap_back, R.id.ll_poi_write, R.id.tv_baidumap_searchpoi, R.id.tv_baidumap_keyword, R.id.button1, R.id.iv_poi_photo, R.id.tv_poi_name, R.id.ll_poi_panorama, R.id.ll_poi_collection, R.id.ll_poi_share, R.id.ll_poi_go, R.id.iv_poi_phone, R.id.ll_poi_moreinfo})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_baidumap_searchpoi:
                PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(mTvBaidumapKeyword.getText().toString())
                        .sortType(PoiSortType.distance_from_near_to_far).location(mCenter)
                        .radius(5000).pageNum(1).pageCapacity(50);
                mPoiSearch.searchNearby(nearbySearchOption);
                break;
            case R.id.tv_baidumap_keyword:
                finish();
                break;
            case R.id.iv_baidumap_back:
                finish();
                break;
            case R.id.button1:
                switch (mCurrentMode) {
                    case NORMAL:
                        button1.setBackgroundResource(R.mipmap.follow);
                        mCurrentMode = LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        break;
                    case COMPASS:
                        button1.setBackgroundResource(R.mipmap.location);
                        mCurrentMode = LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder1 = new MapStatus.Builder();
                        builder1.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                        break;
                    case FOLLOWING:
                        button1.setBackgroundResource(R.mipmap.compass);
                        mCurrentMode = LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder2 = new MapStatus.Builder();
                        builder2.overlook(-45);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder2.build()));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.iv_poi_photo:
                // ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
            case R.id.tv_poi_name:
                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_panorama:
                if (isSearch) {
                    intent.putExtra("lat", mLatitudeSearch);
                    intent.putExtra("lon", mLongitudeSearch);
                    intent.setClass(BaiduMapPoiActivity.this, BaiduMapPanoramaActivity.class);
                    startActivity(intent);
                } else {
                    if (lat != 0 && lon != 0) {
                        intent.putExtra("lat", lat);
                        intent.putExtra("lon", lon);
                        intent.setClass(BaiduMapPoiActivity.this, BaiduMapPanoramaActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.showBgResource(BaiduMapPoiActivity.this, "网络错误！");
                    }
                }
                break;
            case R.id.ll_poi_collection:

                if (("").equals(mUserid) || mUserid == "" || mUserid == null) {
                    mLoginDialog.show();
                } else {
                    if (isSearch) {
                        accessNetCollect(mUserid, mName, mAddress, mLatitudeSearch, mLongitudeSearch);
                    } else {
                        accessNetCollect(mUserid, endpoiName, mEndAddress, lat, lon);
                    }
                }
                break;
            case R.id.ll_poi_share:
                if (isSearch) {
                    mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                            .location(new LatLng(mLatitudeSearch, mLongitudeSearch))
                            .name(mNameSearch)
                            .snippet("易行"));
                } else {
                    mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                            .location(new LatLng(mCurrentLat, mCurrentLon))
                            .name(mName)
                            .snippet("易行"));
                }
                break;
            case R.id.ll_poi_go:
                if (isSearch) {
                    com.amap.api.maps.model.LatLng mEndLatlngSearch = new com.amap.api.maps.model.LatLng(mLatitudeSearch, mLongitudeSearch);
                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                            , new AmapNaviParams(new Poi("", null, "")
                                    , null
                                    , new Poi(mNameSearch, mEndLatlngSearch, "")
                                    , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                            , BaiduMapPoiActivity.this);
                } else {
                    com.amap.api.maps.model.LatLng mEndLatlng = new com.amap.api.maps.model.LatLng(lat, lon);
                    com.amap.api.maps.model.LatLng mStartLatlng = new com.amap.api.maps.model.LatLng(mCurrentLat, mCurrentLon);

                    AmapNaviPage.getInstance().showRouteActivity(getApplicationContext()
                            , new AmapNaviParams(new Poi("", null, "")
                                    , null
                                    , new Poi(endpoiName, mEndLatlng, "")
                                    , AmapNaviType.DRIVER).setTheme(AmapNaviTheme.WHITE)
                            , BaiduMapPoiActivity.this);
                }
                break;
            case R.id.iv_poi_phone:
                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_moreinfo:
                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
            case R.id.ll_poi_write:
                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
            case R.id.button2:
                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "待完成");
                break;
        }
    }

    /**
     * 初始化登录对话框
     */
    private void initDialogLogin() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(this), ScreenUtils.getWindowHeight(this));
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.65f);
        params.height = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.45f);
        mLoginDialog = new Dialog(this);
        View view = View.inflate(this, R.layout.dialog_tip_login, null);
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
        intent.setClass(BaiduMapPoiActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    //分享接口回调
    @Override
    public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {

        // 分享短串结果
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过易行与您分享一个POI点详情: " + currentAddr
                + "链接：" + result.getUrl());
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
                + "链接：" + shareUrlResult.getUrl());
        it.setType("text/plain");
        Intent intent = Intent.createChooser(it, "将路线分享到");
        if (null == intent) {
            ToastUtils.showBgResource(BaiduMapPoiActivity.this, "抱歉，分享目标选择失败");
        }
        startActivity(intent);
    }

    @OnClick(R.id.button2)
    public void onViewClicked() {
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            isSearch = false;
            super.onPoiClick(index);

            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            currentAddr = poi.name;
            return true;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCenter = new LatLng(mCurrentLat, mCurrentLon);
            startpoi = location.getAddrStr();
            Log.i("startloc", location.getLatitude() + "---" + location.getCity());
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(mString)
                    .sortType(PoiSortType.distance_from_near_to_far).location(mCenter)
                    .radius(5000).pageNum(1).pageCapacity(50);
            mPoiSearch.searchNearby(nearbySearchOption);
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mPoiSearch.destroy();
        mMapView = null;
        amapTTSController.destroy();
        mLocClient.unRegisterLocationListener(mListener);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//            String string = data.getExtras().getString("keyword", "美食");
//            etBaidumapSearchpoi.setText(string);
//            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(string)
//                    .sortType(PoiSortType.distance_from_near_to_far).location(mCenter)
//                    .radius(5000).pageNum(1).pageCapacity(50);
//            mPoiSearch.searchNearby(nearbySearchOption);
//            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }
//    }

    public void accessNetCollect(String user_id, String name, String address, double lat, double lng) {
        mDialog.show();
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
                        Log.i("CollectMap", response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String code = jsonObject.getString("code");
                            if (code.equals("0")) {
                                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "搜藏成功");
                            } else if (code.equals("2")) {
                                ToastUtils.showBgResource(BaiduMapPoiActivity.this, "已收藏");
                            }
                            mDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showBgResource(BaiduMapPoiActivity.this, "服务器出错！");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showBgResource(BaiduMapPoiActivity.this, "服务器出错！");
                    }
                });

    }

    private void initDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(this), ScreenUtils.getWindowHeight(this));
        params.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(this);
        View view = View.inflate(this, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }
}
