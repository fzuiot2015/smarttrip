package com.qin.map.activity.baidu;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.qin.R;
import com.qin.application.MyApplication;
import com.qin.util.ToastUtils;

/**
 * Created by Administrator on 2018/4/3 0003.
 */

public class BaiduMapPanoramaActivity extends AppCompatActivity {

    private PanoramaView mPanoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        BMapManager bMapManager = new BMapManager(getApplicationContext());
        setContentView(R.layout.activity_baidumap_panaram);
        mPanoView = findViewById(R.id.panorama);
        mPanoView.setPanoramaZoomLevel(5);
//        mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
//        String uid = "9c67c08bf880bed20aa8d4be";
//        mPanoView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_STREET);
        double lon = getIntent().getExtras().getDouble("lon", 119.19956796058);
        double lat = getIntent().getExtras().getDouble("lat", 26.058800952134);
        if (lat == 0 || lon == 0){
            ToastUtils.showBgResource(BaiduMapPanoramaActivity.this,"全景不存在");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaiduMapPanoramaActivity.this.finish();
                }
            },2000);
        }
        mPanoView.setPanorama(lon, lat, PanoramaView.COORDTYPE_BD09LL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }
}
