package com.qin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.util.SPUtils;
import com.qin.util.StringUtils;
import com.qin.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class CarSplashActivity extends AppCompatActivity {
    @BindView(R.id.tv_splash)
    TextView tvSplash;
    private int n = 0;
    private int titleLength = 0;
    private String title = "易行，简单你的生活";
    private static final String tag = "CarSplashActivity";
    /**
     * UPDATE_VERSION 更新版本的code
     */
    protected static final int UPDATE_VERSION = 0;
    /**
     * ENTER_HOME 进入主界面的code
     */
    protected static final int ENTER_MAIN = 1;
    protected static final int ERROR_URL = 2;
    protected static final int ERROR_IO = 3;
    protected static final int ERROR_JSON = 4;
    protected static final int WEB_ERROR = 5;
    private int mLocalVersionCode;
    private String mVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private TextView mSp_tv_versionname;
    private boolean mLogin_info;
    /**
     * handler 消息机制
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    initUpdateDialog();
                    break;
                case ENTER_MAIN:
                    if (mLogin_info) {
                        enterMain();
                    } else {
                        enterLogin();
                    }
                    break;
                case WEB_ERROR:
                    ToastUtils.showBgResource(getApplicationContext(), "服务器维护中");
                    if (mLogin_info) {
                        enterMain();
                    } else {
                        enterLogin();
                    }
                case ERROR_URL:
                    ToastUtils.showBgResource(getApplicationContext(), "服务器维护中");
                    if (mLogin_info) {
                        enterMain();
                    } else {
                        enterLogin();
                    }
                    break;
                case ERROR_IO:
                    ToastUtils.showBgResource(getApplicationContext(), "服务器维护中");
                    if (mLogin_info) {
                        enterMain();
                    } else {
                        enterLogin();
                    }
                    break;
                case ERROR_JSON:
                    ToastUtils.showBgResource(getApplicationContext(), "JSON错误");
                    if (mLogin_info) {
                        enterMain();
                    } else {
                        enterLogin();
                    }
                    break;
            }
        }

        ;
    };
    private ProgressBar mSp_pb_download;
    private ProgressBar mSp_pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startShowText();
            }
        }, 400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterMain();
            }
        }, 2000);
//        mLogin_info = false;
//        initData();
    }

    /**
     * 显示缓冲界面标题
     */
    private void startShowText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String text = title.substring(0, n);
                    tvSplash.post(new Runnable() {
                        @Override
                        public void run() {
                            tvSplash.setText(text);
                            Log.i("text", n + "");
                        }
                    });
                    SystemClock.sleep(150);
                    n++;
                    if (n <= title.length()) {
                        startShowText();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 显示版本更新对话框
     */
    public void initUpdateDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.7f);
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final Dialog dialog = new Dialog(CarSplashActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_version_upgrde, null, false);
        dialog.setContentView(view, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mLogin_info) {
                    enterMain();
                } else {
                    enterLogin();
                }
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //   downLoad();
            }
        });
    }

    /**
     * 从服务器下载最新版本的apk
     */
//    protected void downLoad() {
//
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                    + "SmartHome.apk";
//            HttpUtils httpUtils = new HttpUtils();
//            if (mDownloadUrl == null) {
//                mDownloadUrl = "http://172.25.37.93:8080/smarthome/SmartHome.apk";
//            } else {
//
//                httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
//
//                    @Override
//                    public void onSuccess(ResponseInfo<File> responseInfo) {
//                        Log.i(tag, "下载成功");
//                        File file = responseInfo.result;
//                        insatllApk(file);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException httpException, String string) {
//                        Log.i(tag, "下载失败");
//                        if (mLogin_info) {
//                            enterMain();
//                        } else {
//                            enterLogin();
//                        }
//                        ToastUtil.showToast(getApplicationContext(), "网络错误");
//                    }
//
//                    @Override
//                    public void onStart() {
//                        Log.i(tag, "开始下载");
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onLoading(final long total, final long current, final boolean isUploading) {
//                        Log.i(tag, "下载中。。。。。。。。。。");
//                        Log.i(tag, "下载总长：  " + total);
//                        Log.i(tag, "下载位置：  " + current);
//
//                        mSp_pb_download.setVisibility(View.VISIBLE);
//                        mSp_pb.setVisibility(View.GONE);
//                        mSp_tv_versionname.setVisibility(View.GONE);
//                        mSp_pb_download.setMax((int) total);
//                        mSp_pb_download.setProgress((int) current);
//                        super.onLoading(total, current, isUploading);
//                    }
//                });
//            }
//        }
//    }

    /**
     * 安装服务器获取的apk
     *
     * @param file
     */
    protected void insatllApk(File file) {
        // 系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
		  //文件作为数据源 intent.setData(Uri.fromFile(file)); //设置安装的类型
		 // intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        // startActivity(intent);
        startActivityForResult(intent, 0);
    }

    // 开启一个activity后,返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterLogin();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mSp_tv_versionname.setText("版本信息: " + getVersionName());
        // 本地版本号
        mLocalVersionCode = getVersionCode();
        System.out.println(mLocalVersionCode + "----------------------------------");
        // 服务端获取版本号
        if (SPUtils.getInstance(this).getBoolean(ConstantValues.OPEN_UPDATE, true)) {
            checkVersionCode();
        } else
            // handler实现延迟执行
            mHandler.sendEmptyMessageDelayed(ENTER_MAIN, 3000);
    }

    /**
     * 服务端获取版本号，访问网络
     */
    private void checkVersionCode() {
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    // URL url = new
                    // URL("http://172.25.12.125:8080/smarthome/smartHomeVersion.json");
                    URL url = new URL("http://192.168.191.1:8080/RegisterAndLogin/smartHomeVersionAll.json");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setConnectTimeout(5000);
                    http.setReadTimeout(3000);
                    http.setRequestMethod("GET");
                    if (http.getResponseCode() == 200) {
                        InputStream in = http.getInputStream();
                        String json = StringUtils.stream2String(in);
                        System.out.println("-------------------" + json);
                        JSONObject jsonObject = new JSONObject(json);
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        mVersionCode = jsonObject.getString("versionCode");
                        mVersionDes = jsonObject.getString("versionDes");
                        jsonObject.getString("versionName");
                        Log.i(tag, jsonObject.getString("downloadUrl"));
                        Log.i(tag, jsonObject.getString("versionCode"));
                        Log.i(tag, jsonObject.getString("versionDes"));
                        Log.i(tag, jsonObject.getString("versionName"));
                        if (mLocalVersionCode < Integer.parseInt(mVersionCode)) {
                            msg.what = UPDATE_VERSION;
                        } else {
                            msg.what = ENTER_MAIN;
                        }
                    } else {
                        msg.what = WEB_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = ERROR_URL;

                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = ERROR_IO;

                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = ERROR_JSON;
                } finally {
                    long endTime = System.currentTimeMillis();
                    if ((endTime - startTime) < 3000) {
                        try {
                            Thread.sleep(3000 - (startTime - endTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取版本号
     *
     * @return int versionCode
     */
    private int getVersionCode() {
        PackageManager pa = getPackageManager();
        try {
            PackageInfo info = pa.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return string versionName
     */
    private String getVersionName() {
        PackageManager pa = getPackageManager();
        try {
            PackageInfo info = pa.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取版本出错";
    }

    /**
     * 跳转到登录界面
     */
    public void enterLogin() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到主界面
     */
    public void enterMain() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
