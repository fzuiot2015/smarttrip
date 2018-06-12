package com.qin.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qin.recevier.NetworkChangedReceiver;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class BaseActivity extends AppCompatActivity {

    private NetworkChangedReceiver mNetworkChangedReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkChangedReceiver = new NetworkChangedReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangedReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNetworkChangedReceiver);
        super.onDestroy();
//        TSnackbar.make(viewGroup, "Top SnackBar !!!!", TSnackbar.LENGTH_SHORT, TSnackbar.APPEAR_FROM_TOP_TO_DOWN)
//                .setBackgroundColor(getResources().getColor(R.color.blue))
//                .show();
    }
}
