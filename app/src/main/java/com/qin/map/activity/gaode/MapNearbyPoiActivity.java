package com.qin.map.activity.gaode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.qin.R;
import com.qin.map.activity.baidu.BaiduMapPanoramaActivity;
import com.qin.map.activity.baidu.BaiduMapSearchPoiActivity;
import com.qin.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MapNearbyPoiActivity extends Activity implements AMap.OnMyLocationChangeListener,
        AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.OnMarkerClickListener,
        PoiSearch.OnPoiSearchListener, AMap.InfoWindowAdapter {

    private AMap aMap;
    private MapView mapView;
    private MyLocationStyle myLocationStyle;
    private EditText mEt_baidumap_searchpoi;
    private double mCurrentLat;
    private double mCurrentLon;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);// 116.472995,39.993743
    private Marker locationMarker; // 选择的点
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据
    private String keyWord = "";
    private String mCurrentCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_nearbypoi);
        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
//        mCurrentCity = getIntent().getExtras().getString("city","福州");
        init();
        findViewById(R.id.tv_search).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MapNearbyPoiActivity.this, BaiduMapPanoramaActivity.class);
//                startActivity(intent);

                doSearchQuery();
            }
        });
        mEt_baidumap_searchpoi = findViewById(R.id.et_baidumap_searchpoi);
        mEt_baidumap_searchpoi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLat == 0 || mCurrentLon == 0) {
                    ToastUtils.showBgResource(MapNearbyPoiActivity.this, "定位失败！请重新定位。");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("latitude", mCurrentLat);
                    intent.putExtra("longitude", mCurrentLon);
                    intent.setClass(MapNearbyPoiActivity.this, BaiduMapSearchPoiActivity.class);
                    startActivityForResult(intent, 100);
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoBottomMargin(AMapOptions.LOGO_MARGIN_BOTTOM - 100);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

//        // 只定位，不进行其他操作
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
//        // 设置定位的类型为 跟随模式
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));
//        // 设置定位的类型为根据地图面向方向旋转
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE));
//        // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
//        // 定位、但不会移动到地图中心点，并且会跟随设备移动。
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER));
//        // 定位、但不会移动到地图中心点，地图依照设备方向旋转，并且会跟随设备移动。
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER));
//        // 定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
//        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyWord = mEt_baidumap_searchpoi.getText().toString().trim();
        if ("".equals(keyWord)) {
            ToastUtils.showBgResource(MapNearbyPoiActivity.this,"请输入搜索关键字");
        } else {
            currentPage = 0;
            query = new PoiSearch.Query(keyWord, "", mCurrentCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);// 设置查第一页

            if (mCurrentLat != 0 && mCurrentLon != 0) {
                poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mCurrentLat, mCurrentLon), 10000, true));//
                poiSearch.searchPOIAsyn();// 异步搜索
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String string = data.getExtras().getString("keyword", "美食");
            mEt_baidumap_searchpoi.setText(string);
            String city = "福州";

        }
    }

    @Override
    public void onMyLocationChange(Location location) {
// 定位回调监听
        if (location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);

                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                mCurrentLat = location.getLatitude();
                mCurrentLon = location.getLongitude();
                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlay != null) {
                            poiOverlay.removeFromMap();
                        }
                        aMap.clear();
                        poiOverlay = new myPoiOverlay(aMap, poiItems);
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

//                        aMap.addMarker(new MarkerOptions()
//                                .anchor(0.5f, 0.5f)
//                                .icon(BitmapDescriptorFactory
//                                        .fromBitmap(BitmapFactory.decodeResource(
//                                                getResources(), R.drawable.point4)))
//                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));

//						mAMap.addCircle(new CircleOptions()
//						.center(new LatLng(lp.getLatitude(),
//								lp.getLongitude())).radius(5000)
//						.strokeColor(Color.BLUE)
//						.fillColor(Color.argb(50, 1, 1, 1))
//						.strokeWidth(2));

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtils.showBgResource(MapNearbyPoiActivity.this,
                                "未搜索到结果");
                    }
                }
            } else {
                ToastUtils.showBgResource(MapNearbyPoiActivity.this, "未搜索到结果");
            }
        } else {
            ToastUtils.showBgResource(this.getApplicationContext(), String.valueOf(rcode));
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getObject() != null) {
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.mipmap.poi_marker_pressed)));

                setPoiItemDisplayContent(mCurrentPoi);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            resetlastmarker();
        }


        return true;
    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        } else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed)));
        }
        mlastMarker = null;

    }


    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("\nadName-------");
        sb.append(mCurrentPoi.getAdName());
        sb.append("\nadCode-------");
        sb.append(mCurrentPoi.getAdCode());
        sb.append("\nbusinessArea-------");
        sb.append(mCurrentPoi.getBusinessArea());
        sb.append("\ncityName-------");
        sb.append(mCurrentPoi.getCityName());
        sb.append("\ndirection-------");
        sb.append(mCurrentPoi.getDirection());
        sb.append("\ndistance-------");
        sb.append(mCurrentPoi.getDistance());
        sb.append("\nemail-------");
        sb.append(mCurrentPoi.getEmail());
        sb.append("\nindoorData-------");
        sb.append(mCurrentPoi.getIndoorData().getFloor());
        sb.append("\nparkingType-------");
        sb.append(mCurrentPoi.getParkingType());
        sb.append("\nphotosUrl-------");
        sb.append(mCurrentPoi.getPhotos().get(0).getUrl());
        sb.append("\nshopID-------");
        sb.append(mCurrentPoi.getShopID());
        sb.append("\ntel-------");
        sb.append(mCurrentPoi.getTel());
        sb.append("\ntypeDes-------");
        sb.append(mCurrentPoi.getTypeDes());
        sb.append("\nwebsite-------");
        sb.append(mCurrentPoi.getWebsite());
        sb.append("\ntitle-------");
        sb.append(mCurrentPoi.getTitle());
        String result = sb.toString();
        Log.i("poi", result);
    }


    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    private int[] markers = {R.mipmap.poi_marker_1,
            R.mipmap.poi_marker_2,
            R.mipmap.poi_marker_3,
            R.mipmap.poi_marker_4,
            R.mipmap.poi_marker_5,
            R.mipmap.poi_marker_6,
            R.mipmap.poi_marker_7,
            R.mipmap.poi_marker_8,
            R.mipmap.poi_marker_9,
            R.mipmap.poi_marker_10
    };

    @Override
    public void onMapClick(LatLng arg0) {
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName();
        }
        ToastUtils.showBgResource(this, infomation);
    }


    /**
     * 自定义PoiOverlay
     */

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

        public myPoiOverlay(AMap amap, List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         *
         * @since V2.1.0
         */
        public void addToMap() {
            for (int i = 0; i < mPois.size(); i++) {
                Marker marker = mamap.addMarker(getMarkerOptions(i));
                PoiItem item = mPois.get(i);
                marker.setObject(item);
                mPoiMarks.add(marker);
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0; i < mPois.size(); i++) {
                b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                        mPois.get(i).getLatLonPoint().getLongitude()));
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        private String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        private String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         */
        private int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         *
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        private BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            } else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.poi_marker_pressed));
                return icon;
            }
        }
    }
}
