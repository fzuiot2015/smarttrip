package com.qin.activity.consume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.qin.R;
import com.qin.fragment.drawer.consume.MonthConsumeFragment;

/**
 * Created by Administrator on 2018/5/6.
 */

public class MonthConsumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_month,new MonthConsumeFragment()).commit();
    }
}
