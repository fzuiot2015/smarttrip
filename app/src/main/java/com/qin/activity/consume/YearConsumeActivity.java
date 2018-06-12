package com.qin.activity.consume;


import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qin.R;
import com.qin.fragment.drawer.consume.YearConsumeFragment;

/**
 * Created by Administrator on 2018/5/6.
 */

public class YearConsumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_year,new YearConsumeFragment()).commit();
    }
}
