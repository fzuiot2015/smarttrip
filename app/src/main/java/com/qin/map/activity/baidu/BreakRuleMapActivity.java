package com.qin.map.activity.baidu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.qin.R;
import com.qin.pojo.breakrule.Result;
import com.qin.util.ScreenUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26.
 */

public class BreakRuleMapActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mapview)
    MapView mMapview;
    @BindView(R.id.tv_breakrule_city)
    TextView mTvBreakruleCity;
    @BindView(R.id.tv_breakrule_address)
    TextView mTvBreakruleAddress;
    @BindView(R.id.tv_breakrule_content)
    TextView mTvBreakruleContent;
    @BindView(R.id.tv_breakrule_distance)
    TextView mTvBreakruleDistance;
    @BindView(R.id.tv_breakrule_num)
    TextView mTvBreakruleNum;
    @BindView(R.id.refresh_breakrule_map)
    SmartRefreshLayout mRefreshBreakruleMap;
    private BaiduMap mBaiduMap;
    private Result mResult;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap_breakrule);
        ButterKnife.bind(this);
        mBaiduMap = mMapview.getMap();
        initDialog(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mResult = getIntent().getExtras().getParcelable("result");
        Log.i("经纬度", "---" + mResult.getLat());
        MapStatus.Builder builder = new MapStatus.Builder();
        final LatLng latlngSearch = new LatLng(Double.parseDouble(mResult.getLat()), Double.parseDouble(mResult.getLng()));
        builder.target(latlngSearch).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.maker);
        MarkerOptions maker = new MarkerOptions().position(latlngSearch).icon(bitmapDescriptor).draggable(false);
        maker.animateType(MarkerOptions.MarkerAnimateType.drop);
        mBaiduMap.addOverlay(maker);

        mTvBreakruleCity.setText("当前所在城市 ： "+mResult.getProvince()+"省"+mResult.getCity()+"市");
        mTvBreakruleAddress.setText(mResult.getAddress());
        mTvBreakruleContent.setText(mResult.getContent());
        mTvBreakruleDistance.setText(mResult.getDistance()+"米");
        mTvBreakruleNum.setText("该地违章次数 : "+mResult.getNum());
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
}
