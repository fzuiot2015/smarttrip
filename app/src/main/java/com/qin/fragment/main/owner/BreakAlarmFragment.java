package com.qin.fragment.main.owner;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.breakalarmresult.BreakResultRecyclerViewAdapter;
import com.qin.adapter.recyclerview.breakalarmresult.BreakResultRecyclerViewAdapter1;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.fragment.BaseFragment;
import com.qin.pojo.BreakAlarm.BreakAlarm;
import com.qin.pojo.breakalarminfo.BreakAlarmInfo;
import com.qin.pojo.breakalarminfo.Police;
import com.qin.pojo.breakalarminfo.Rescue;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class BreakAlarmFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_alarm_location)
    TextView mTvAlarmLocation;
    @BindView(R.id.iv_alarm_agiainloc)
    ImageView mIvAlarmAgiainloc;
    @BindView(R.id.tv_alarm_agiainloc)
    TextView mTvAlarmAgiainloc;
    @BindView(R.id.et_alarm_describe)
    EditText mEtAlarmDescribe;
    @BindView(R.id.et_alarm_time)
    EditText mEtAlarmTime;
    @BindView(R.id.iv_alarm_car)
    ImageView mIvAlarmCar;
    @BindView(R.id.et_alarm_car)
    EditText mEtAlarmCar;
    @BindView(R.id.iv_alarm_person)
    ImageView mIvAlarmPerson;
    @BindView(R.id.et_alarm_person)
    EditText mEtAlarmPerson;
    @BindView(R.id.et_alarm_other)
    EditText mEtAlarmOther;
    @BindView(R.id.btn_alarm_commit)
    Button mBtnAlarmCommit;
    Unbinder unbinder;
    @BindView(R.id.tv_3)
    TextView mTv3;
    @BindView(R.id.tv_4)
    TextView mTv4;
    @BindView(R.id.tv_1)
    TextView mTv1;
    @BindView(R.id.tv_2)
    TextView mTv2;
    @BindView(R.id.recyclerView_breakalarm)
    RecyclerView mRecyclerViewBreakalarm;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingLayout;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String mUserid;
    private String mSpUser_id;
    private Dialog mDialog;
    private double mLongitude;
    private double mLatitude;
    private BreakAlarmInfo mBreakAlarmInfo;
    private List<Police> mPolice;
    private List<Rescue> mRescue;
    private BreakResultRecyclerViewAdapter mPoliceAdapter;
    private BreakResultRecyclerViewAdapter1 mRescueAdapter;


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
        View view = inflater.inflate(R.layout.fragment_owner_breakalarm, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        mIvAlarmCar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        mTvAlarmLocation.setSelected(true);
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
                    mTvAlarmLocation.setText(location.getAddress());
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                } else {
                    mTvAlarmAgiainloc.setText("定位失败");
                }
                mTvAlarmAgiainloc.setText("重新定位");
            }
        }
    };

    private void accessNet() {
        mDialog.show();
        BreakAlarm breakAlarm = new BreakAlarm();
        breakAlarm.setAddress(mTvAlarmLocation.getText().toString().trim());
        breakAlarm.setDes_car(mEtAlarmCar.getText().toString().trim());
        breakAlarm.setDes_person(mEtAlarmPerson.getText().toString().trim());
        breakAlarm.setDes_others(mEtAlarmOther.getText().toString().trim());
        breakAlarm.setDes_casual(mEtAlarmDescribe.getText().toString().trim());
        breakAlarm.setUser_id(Integer.parseInt(mUserid));
        breakAlarm.setLat(mLatitude + "");
        breakAlarm.setLng(mLongitude + "");
        String json = new Gson().toJson(breakAlarm);
        OkGo.<String>post(ConstantValues.URL_BREAKALARM)
                .tag(this)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("breakalarm", response.body());
                        mDialog.dismiss();
                        String body = response.body();
                        parseData(body);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(mActivity, "网络错误！");
                    }
                });
    }

    private void parseData(String json) {
        Gson gson = new Gson();
        mBreakAlarmInfo = gson.fromJson(json, BreakAlarmInfo.class);
        String code = mBreakAlarmInfo.getCode();
        if (code.equals("0")) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mPolice = mBreakAlarmInfo.getPolice();
            mRescue = mBreakAlarmInfo.getRescue();

            mTv3.setText("保险公司名称 ： " + mBreakAlarmInfo.getBapxian_name());
            mTv4.setText(mBreakAlarmInfo.getBapxian_tel());

            LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
            mRecyclerViewBreakalarm.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewBreakalarm.addItemDecoration(new MarginDecorationVertical(20));
            mRecyclerViewBreakalarm.setLayoutManager(layoutManager);
            mPoliceAdapter = new BreakResultRecyclerViewAdapter(mPolice);
            mRescueAdapter = new BreakResultRecyclerViewAdapter1(mRescue);
            mRecyclerViewBreakalarm.setAdapter(mPoliceAdapter);
            mPoliceAdapter.setOnItemClickListener(new BreakResultRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, BreakResultRecyclerViewAdapter.ViewName viewName, int postion) {
                    long phone = Long.parseLong(mPolice.get(postion).getTel());
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        } else {
            ToastUtils.showBgResource(mActivity, "服务器出错！");
        }
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

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_alarm_agiainloc, R.id.iv_alarm_car, R.id.iv_alarm_person, R.id.btn_alarm_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_alarm_agiainloc:
                startLocation();
                mTvAlarmAgiainloc.setText("正在定位...");
                break;
            case R.id.iv_alarm_car:
                RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mActivity, TITLE);
                dialogChooseImage.show();
                break;
            case R.id.iv_alarm_person:
                break;
            case R.id.btn_alarm_commit:
                accessNet();
                break;
            case R.id.tv_1:
                mRecyclerViewBreakalarm.setVisibility(View.VISIBLE);
                mTv1.setTextColor(getResources().getColor(R.color.red));
                mTv2.setTextColor(getResources().getColor(R.color.black));
                mRecyclerViewBreakalarm.setAdapter(mPoliceAdapter);
                mPoliceAdapter.setOnItemClickListener(new BreakResultRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, BreakResultRecyclerViewAdapter.ViewName viewName, int postion) {
                        long phone = Long.parseLong(mPolice.get(postion).getTel());
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.tv_2:
                mRecyclerViewBreakalarm.setVisibility(View.GONE);
                mTv2.setTextColor(getResources().getColor(R.color.red));
                mTv1.setTextColor(getResources().getColor(R.color.black));
                mRecyclerViewBreakalarm.setAdapter(mRescueAdapter);
                mRescueAdapter.setOnItemClickListener(new BreakResultRecyclerViewAdapter1.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, BreakResultRecyclerViewAdapter1.ViewName viewName, int postion) {
                        long phone = Long.parseLong(mRescue.get(postion).getTel());
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;

            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                Glide.with(mActivity).
                        load(RxPhotoTool.cropImageUri).
                        into(mIvAlarmCar);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    roadImageView( UCrop.getOutput(data), mIvAlarmCar);
                    RxSPTool.putContent(mActivity, "AVATAR", UCrop.getOutput(data).toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(mActivity).
                load(uri).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(mActivity, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(mActivity.getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(mActivity);
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
}
