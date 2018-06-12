package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.breakrepair.BreakRepairResultRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.breakrepair.BreakRepair;
import com.qin.pojo.usersearch.repairshop.Contents;
import com.qin.pojo.usersearch.repairshop.RepairShop;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakRepairFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    Unbinder unbinder;
    @BindView(R.id.tv_breakrepair_location)
    TextView mTvBreakrepairLocation;
    @BindView(R.id.iv_breakrepair_agiainloc)
    ImageView mIvBreakrepairAgiainloc;
    @BindView(R.id.tv_breakrepair_agiainloc)
    TextView mTvBreakrepairAgiainloc;
    @BindView(R.id.et_breakrepair_time)
    EditText mEtBreakrepairTime;
    @BindView(R.id.et_breakrepair_model)
    EditText mEtBreakrepairModel;
    @BindView(R.id.iv_breakrepair_car)
    ImageView mIvBreakrepairCar;
    @BindView(R.id.et_breakrepair_des)
    EditText mEtBreakrepairDes;
    @BindView(R.id.et_breakrepair_other)
    EditText mEtBreakrepairOther;
    @BindView(R.id.btn_breakrepair_commit)
    Button mBtnBreakrepairCommit;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingLayout;
    private String mUserid;
    private Dialog mDialog;
    private String mSpUser_id;
    private double mLongitude;
    private double mLatitude;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption locationOption = null;
    private List<Contents> mContents;
    private BreakRepairResultRecyclerViewAdapter mAdapter;

    @Override
    public View initView() {
        return null;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_breakrepair, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        mTvBreakrepairLocation.setSelected(true);
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        initLocation();
        initDialog();
    }

    /**
     * 初始化位置信息
     */
    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(mActivity);
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
                    mTvBreakrepairLocation.setText(location.getAddress());
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                } else {
                    mTvBreakrepairAgiainloc.setText("定位失败");
                }
                mTvBreakrepairAgiainloc.setText("重新定位");
            }
        }
    };

    private void accessNetPic(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.amap_man);
        String base64 = bitmapToBase64(bitmap);
        OkGo.<String>post(ConstantValues.URL_BREAKALARMPIC)
                .tag(this)
                .params("pic_car",base64)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("pic_car", response.body());
                        mDialog.dismiss();
                        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "网络错误！");
                    }
                });
    }

    private void accessNet() {
        mDialog.show();
        BreakRepair breakRepair = new BreakRepair();
        breakRepair.setAddress(mTvBreakrepairLocation.getText().toString().trim());
        breakRepair.setLat(mLatitude + "");
        breakRepair.setUser_id(Integer.parseInt(mUserid));
        breakRepair.setLng(mLongitude + "");
        breakRepair.setDescription(mEtBreakrepairDes.getText().toString().trim());
        breakRepair.setModels(mEtBreakrepairModel.getText().toString().trim());
        breakRepair.setOthers(mEtBreakrepairOther.getText().toString().trim());
        breakRepair.setTime(mEtBreakrepairTime.getText().toString().trim());
        String json = new Gson().toJson(breakRepair);
        OkGo.<String>post(ConstantValues.URL_BREAKREPAIR)
                .tag(this)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("breakrepair", response.body());
                        String body = response.body();
                        parseData(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "服务器繁忙");
                    }
                });
    }

    private void parseData(String json) {
        accessNetPic();
        Gson gson = new Gson();
        RepairShop repairShop = gson.fromJson(json, RepairShop.class);
        mContents = repairShop.getContents();
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MarginDecorationVertical(20));
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BreakRepairResultRecyclerViewAdapter(mContents);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BreakRepairResultRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BreakRepairResultRecyclerViewAdapter.ViewName viewName, int postion) {
                long phone = Long.parseLong(mContents.get(postion).getGarage_tel()+"");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void initDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(mActivity), ScreenUtils.getWindowHeight(mActivity));
        params.width = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(mActivity);
        View view = View.inflate(mActivity, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @OnClick({R.id.tv_breakrepair_agiainloc, R.id.iv_breakrepair_car, R.id.btn_breakrepair_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_breakrepair_agiainloc:
                startLocation();
                mTvBreakrepairAgiainloc.setText("正在定位...");
                break;
            case R.id.iv_breakrepair_car:
                break;
            case R.id.btn_breakrepair_commit:
                if (TextUtils.isEmpty(mTvBreakrepairLocation.getText().toString().trim())){
                    ToastUtils.showBgResource(mActivity,"请输入位置信息");
                }else if (TextUtils.isEmpty(mEtBreakrepairTime.getText().toString().trim())){
                    ToastUtils.showBgResource(mActivity,"请输入时间");
                }else{
                    accessNet();
                }
                break;
        }
    }
}
