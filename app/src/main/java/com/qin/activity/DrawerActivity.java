package com.qin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.fragment.drawer.LocationTipFragment;
import com.qin.fragment.drawer.MyCarFragment;
import com.qin.fragment.drawer.MyCollectionFragment;
import com.qin.fragment.drawer.PersonalInfoFragment;
import com.qin.fragment.drawer.ProducerFragment;
import com.qin.fragment.drawer.ProxyFragment;
import com.qin.fragment.drawer.UseGuideFragment;
import com.qin.fragment.drawer.history.HistoryNoDataFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class DrawerActivity extends AppCompatActivity {
    @BindView(R.id.fl_drawer)
    FrameLayout fl_drawer;
    private int number;
    private FragmentTransaction mBeginTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        number = getIntent().getExtras().getInt(ConstantValues.DRAWERNUMBER);
        if (number == 10) {
            showFragment(new PersonalInfoFragment());
            return;
        }
        if (number == 11) {
            showFragment(new MyCarFragment());
            return;
        }
        if (number == 13) {
            showFragment(new HistoryNoDataFragment());
            return;
        }
        if (number == 15) {
            showFragment(new LocationTipFragment());
            return;
        }
        if (number == 16) {
            showFragment(new MyCollectionFragment());
            return;
        }
        if (number == 17) {
            showFragment(new UseGuideFragment());
            return;
        }
        if (number == 20) {
            showFragment(new ProducerFragment());
            return;
        }
        if (number == 21) {
            showFragment(new ProxyFragment());
            return;
        }
    }

    public void showFragment(Fragment fragment) {
        mBeginTransaction = getSupportFragmentManager().beginTransaction();
        mBeginTransaction.replace(R.id.fl_drawer, fragment);
        mBeginTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
