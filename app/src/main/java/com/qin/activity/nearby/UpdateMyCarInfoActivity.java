package com.qin.activity.nearby;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qin.R;
import com.qin.application.MyApplication;
import com.qin.constant.ConstantValues;
import com.qin.pojo.mycar.MyCarInfo;
import com.qin.pojo.mycar.MyCarInfoUpdate;
import com.qin.util.SPUtils;
import com.qin.util.ScreenUtils;
import com.qin.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by Administrator on 2018/5/9.
 */

public class UpdateMyCarInfoActivity extends AppCompatActivity {

    @BindView(R.id.user_engine)
    EditText mUserEngine;
    @BindView(R.id.user_chepai)
    EditText mUserChepai;
    @BindView(R.id.user_producer)
    EditText mUserProducer;
    @BindView(R.id.user_chejia)
    EditText mUserChejia;
    @BindView(R.id.user_model_name)
    EditText mUserModelName;
    @BindView(R.id.user_dealer)
    EditText mUserDealer;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_alter)
    Button mBtnAlter;
    @BindView(R.id.iv_mycar_plate_update)
    ImageView mIvMycarPlateUpdate;
    private String mUserid;
    private String mSpUser_id;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mycarupdate);
        ButterKnife.bind(this);
        mUserid = MyApplication.getUserid();
        mSpUser_id = SPUtils.getInstance(this).getString("user_id", "");
        if (mUserid == null) {
            mUserid = mSpUser_id;
        }

        initDialog();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        accessNet(mUserid);

    }

    private void accessNetPic() {
        BitmapDrawable background_car = (BitmapDrawable) mIvMycarPlateUpdate.getDrawable();
        Bitmap bitmap_car = background_car.getBitmap();
        String base64_car = bitmapToBase64(bitmap_car);
        OkGo.<String>post(ConstantValues.URL_MYCARUPDATEPIC)
                .tag(this)
                .params("user_id", mUserid)
                .params("register_pic", base64_car)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("register_pic", response.body());
                        mDialog.dismiss();
                        ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "修改成功！");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "网络错误！");
                    }
                });
    }

    private void accessNet(String userid) {
        mDialog.show();
        OkGo.<String>post(ConstantValues.URL_MYCAR)
                .tag(this)
                .params("user_id", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Personalinfo", response.body());
                        mDialog.dismiss();
                        String body = response.body();
                        try {
                            parseeData(body);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "网络错误！");
                    }
                });
    }

    private void parseeData(String json) {
        Gson gson = new Gson();
        MyCarInfo myCarInfo = gson.fromJson(json, MyCarInfo.class);
        mUserChejia.setText(myCarInfo.getUser_chejia());
        mUserChepai.setText(myCarInfo.getUser_chepai());
        mUserDealer.setText(myCarInfo.getUser_dealer());
        mUserEngine.setText(myCarInfo.getUser_engine());
        mUserModelName.setText(myCarInfo.getUser_model_name());
        mUserProducer.setText(myCarInfo.getUser_producer());
        Glide.with(this).load(myCarInfo.getRegister_pic()).placeholder(R.mipmap.gas_blank).into(mIvMycarPlateUpdate);
    }

    private void accessNetAlter(String userid) {
        mDialog.show();
        Gson gson = new Gson();
        MyCarInfoUpdate myCarInfoUpdate = new MyCarInfoUpdate();
        myCarInfoUpdate.setUser_id(mUserid);
        myCarInfoUpdate.setUser_chejia(mUserChejia.getText().toString());
        myCarInfoUpdate.setUser_chepai(mUserChepai.getText().toString());
        myCarInfoUpdate.setUser_dealer(mUserDealer.getText().toString());
        myCarInfoUpdate.setUser_producer(mUserProducer.getText().toString());
        myCarInfoUpdate.setUser_engine(mUserEngine.getText().toString());
        myCarInfoUpdate.setUser_model_name(mUserModelName.getText().toString());
        String toJson = gson.toJson(myCarInfoUpdate);

        OkGo.<String>post(ConstantValues.URL_MYCARUUPDATE)
                .tag(this)
                .upJson(toJson)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i("Personalinfo", response.body());
                        String body = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(body);
                            String code = jsonObject.getString("code");
                            Log.i("MyCarUpdateFragment", "code======" + code);
                            if (code.equals("0")) {
                                accessNetPic();
                           //     ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "修改成功！");
                            } else {
                                mDialog.dismiss();
                                ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "修改失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "网络错误！");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mDialog.dismiss();
                        ToastUtils.showBgResource(UpdateMyCarInfoActivity.this, "网络错误！");
                    }
                });
    }


    @OnClick({R.id.btn_alter, R.id.iv_mycar_plate_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_alter:
                mUserChejia.getText().toString();
                mUserChepai.getText().toString();
                mUserDealer.getText().toString();
                mUserEngine.getText().toString();
                mUserModelName.getText().toString();
                mUserProducer.getText().toString();
                accessNetAlter(mUserid);
                break;
            case R.id.iv_mycar_plate_update:
                RxDialogChooseImage dialogChooseImage1 = new RxDialogChooseImage(UpdateMyCarInfoActivity.this, TITLE);
                dialogChooseImage1.show();
                break;
        }
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getWindowWidth(UpdateMyCarInfoActivity.this), ScreenUtils.getWindowHeight(UpdateMyCarInfoActivity.this));
        params.width = (int) (UpdateMyCarInfoActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        params.height = (int) (UpdateMyCarInfoActivity.this.getWindowManager().getDefaultDisplay().getWidth() * 0.5f);
        mDialog = new Dialog(UpdateMyCarInfoActivity.this);
        View view = View.inflate(UpdateMyCarInfoActivity.this, R.layout.dialog_loading, null);
        mDialog.setContentView(view);
        mDialog.setContentView(view, params);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                        into(mIvMycarPlateUpdate);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP:
                //UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mIvMycarPlateUpdate);
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
        Glide.with(UpdateMyCarInfoActivity.this).
                load(uri).
                asBitmap().
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(UpdateMyCarInfoActivity.this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(UpdateMyCarInfoActivity.this.getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(UpdateMyCarInfoActivity.this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(UpdateMyCarInfoActivity.this, R.color.colorPrimaryDark));

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
    }
}
