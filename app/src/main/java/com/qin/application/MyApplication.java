package com.qin.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.qin.R;

import com.vondear.rxtools.RxTool;
import com.weavey.loading.lib.LoadingLayout;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class MyApplication extends Application {

    private int count = 0;

    private static String mUserid;
    @Override
    public void onCreate() {
        super.onCreate();
      //  Thread.setDefaultUncaughtExceptionHandler(this);
        SDKInitializer.initialize(getApplicationContext());
     //   LeakCanary.install(this);
        RxTool.init(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                count ++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(count > 0) {
                    count--;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
        initOkgo();
        initLoadingLayout();
    }
//    /**
//     * 当有异常产生时执行该方法
//     * @param thread 当前线程
//     * @param ex 异常信息
//     */
//    @Override
//    public void uncaughtException(final Thread thread, final Throwable ex) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("TAG","currentThread:"+Thread.currentThread()+"---thread:"+thread.getId()+"---ex:"+ex.toString());
//            }
//        }).start();
//        SystemClock.sleep(2000);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

    private void initOkgo(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志

        //超时时间设置，默认60秒
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));

        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();

        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }

    //loading加载错误。无数据。网络界面
    private void initLoadingLayout(){
        LoadingLayout.getConfig()
                .setErrorText("出错啦~亲稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.empty)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.deepgray)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.deepgray)
                .setReloadButtonWidthAndHeight(150,40)
                .setAllPageBackgroundColor(R.color.colorWrite);
    }
    /**
     * 判断app是否在后台
     * @return
     */
    public boolean isBackground(){
        if(count <= 0){
            return true;
        } else {
            return false;
        }
    }

    public static void setUserid(String userid){
        mUserid = userid;
    }

    public  static String getUserid(){
        return mUserid;
    }
}
