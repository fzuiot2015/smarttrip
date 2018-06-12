package com.qin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.adapter.recyclerview.MarginDecorationVertical;
import com.qin.adapter.recyclerview.breakrepair.BreakRepairResultRecyclerViewAdapter;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.pojo.breakrepair.BreakRepair;
import com.qin.pojo.usersearch.repairshop.Contents;
import com.qin.pojo.usersearch.repairshop.RepairShop;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by Administrator on 2018/5/9.
 */

public class BreakRepairActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
    @BindView(R.id.dragView)
    LinearLayout mDragView;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingLayout;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String mUserid;
    private String mSpUser_id;
    private Dialog mDialog;
    private double mLongitude;
    private double mLatitude;
    private List<Contents> mContents;
    private BreakRepairResultRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_owner_breakrepair);
        ButterKnife.bind(this);

        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(this).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        mLocationClient = new AMapLocationClient(this);
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

    private void accessNetPic() {
        BitmapDrawable background_car = (BitmapDrawable) mIvBreakrepairCar.getDrawable();
        Bitmap bitmap_car = background_car.getBitmap();
        String base64_car = bitmapToBase64(bitmap_car);
        OkGo.<String>post(ConstantValues.URL_BREAKREPAIRPIC)
                .tag(this)
                .params("fault_photo", base64_car)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("fault_photo", response.body());
                        mDialog.dismiss();
                        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(BreakRepairActivity.this, "网络错误！");
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
                        try {
                            parseData(body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(BreakRepairActivity.this, "服务器繁忙");
                    }
                });
    }

    private void parseData(String json) {
        accessNetPic();
        Gson gson = new Gson();
        RepairShop repairShop = gson.fromJson(json, RepairShop.class);
        mContents = repairShop.getContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MarginDecorationVertical(20));
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BreakRepairResultRecyclerViewAdapter(mContents);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BreakRepairResultRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BreakRepairResultRecyclerViewAdapter.ViewName viewName, int postion) {
                long phone = Long.parseLong(mContents.get(postion).getGarage_tel() + "");
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(BreakRepairActivity.this), ScreenUtils.getWindowHeight(BreakRepairActivity.this));
        params.width = (int) (BreakRepairActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (BreakRepairActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(BreakRepairActivity.this);
        View view = View.inflate(BreakRepairActivity.this, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationOption = null;
        }
    }

    @OnClick({R.id.tv_breakrepair_agiainloc, R.id.iv_breakrepair_car, R.id.btn_breakrepair_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_breakrepair_agiainloc:
                startLocation();
                mTvBreakrepairAgiainloc.setText("正在定位...");
                break;
            case R.id.iv_breakrepair_car:
                RxDialogChooseImage dialogChooseImage1 = new RxDialogChooseImage(BreakRepairActivity.this, TITLE);
                dialogChooseImage1.show();
                break;
            case R.id.btn_breakrepair_commit:
                if (TextUtils.isEmpty(mTvBreakrepairLocation.getText().toString().trim())) {
                    ToastUtils.showBgResource(BreakRepairActivity.this, "请输入位置信息");
                } else if (TextUtils.isEmpty(mEtBreakrepairTime.getText().toString().trim())) {
                    ToastUtils.showBgResource(BreakRepairActivity.this, "请输入时间");
                } else {
                    accessNet();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE:
                //选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                  RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA:
                //选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);
// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE:
                /// /普通裁剪后的处理
                Glide.with(this).
                        load(RxPhotoTool.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(this)).
                        thumbnail(0.5f).
                        placeholder(R.mipmap.upload_camera).
                        priority(Priority.LOW).
                        error(R.mipmap.upload_camera).
                        fallback(R.mipmap.upload_camera).
                        into(mIvBreakrepairCar);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP:
                //UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mIvBreakrepairCar);
                    RxSPTool.putContent(this, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR:
                //UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(BreakRepairActivity.this).
                load(uri).
                asBitmap().
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(BreakRepairActivity.this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(BreakRepairActivity.this.getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(BreakRepairActivity.this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(BreakRepairActivity.this, R.color.colorPrimaryDark));

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
                .start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }
}
