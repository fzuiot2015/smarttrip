package com.qin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.qin.R;
import com.qin.fragment.drawer.UseGuideFragment;

import app.dinus.com.loadingdrawable.LoadingView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UseGuideActivity extends AppCompatActivity {
    @BindView(R.id.tv_main_weather)
    TextView tvMainWeather;
    @BindView(R.id.tv_main_temp)
    TextView tvMainTemp;
    @BindView(R.id.ll_main_weather)
    LinearLayout llMainWeather;
    @BindView(R.id.tv_main_ar)
    TextView tvMainAr;
    @BindView(R.id.ll_main_ar)
    LinearLayout llMainAr;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.drawer_container)
    FrameLayout drawerContainer;
    private AccountHeader headerResult;
    private Drawer result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useguide);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("易行");
        UseGuideFragment useGuideFragment = new UseGuideFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, useGuideFragment).commit();
        initSlidingMenu(savedInstanceState);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        headerResult = new AccountHeaderBuilder()
                .withActivity(UseGuideActivity.this)
                .withCompactStyle(false)
                .withHeaderBackground(R.color.blue)
                .addProfiles(new ProfileDrawerItem().withName("未登录").withEmail("").withIcon(R.mipmap.gas_blank).withIdentifier(100),
                        //   new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.mipmap.gas_blank).withIdentifier(101),
                        new ProfileSettingDrawerItem().withName("添加账号").withDescription("添加一个账号").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(102),
                        new ProfileSettingDrawerItem().withName("管理账号").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(103))
                .withSavedInstance(savedInstanceState)
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("个人信息").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(10),
                        new SecondaryDrawerItem().withName("我的车辆").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(11),
                        new SecondaryDrawerItem().withName("记录中心").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(13),
                        //        new SecondaryDrawerItem().withName("位置提醒点").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(15),
                        new SecondaryDrawerItem().withName("我的收藏").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(16),
                        new SecondaryDrawerItem().withName("使用指南").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(17),
                        new SecondaryDrawerItem().withName("检测更新").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(18),
                        new SecondaryDrawerItem().withName("当前版本").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(19),
                        new SecondaryDrawerItem().withName("开发人员").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(20),
                        //        new SecondaryDrawerItem().withName("设置代理").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(21),
                        new SecondaryDrawerItem().withName("退出账号").withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(22),
                        new SectionDrawerItem()
                )
                .withSavedInstance(savedInstanceState)
                .build();
    }
}
