package com.qin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.qin.R;
import com.qin.constant.ConstantValues;
import com.qin.fragment.main.owner.BreakRuleHappenFragment;
import com.qin.fragment.main.owner.BreakDTCFragment;
import com.qin.fragment.main.owner.BreakRuleQueryFragment;
import com.qin.fragment.main.owner.BreakAlarmFragment;
import com.qin.fragment.main.owner.CarTypeFragment;
import com.qin.fragment.main.owner.EndCodeLimitedFragment;
import com.qin.fragment.main.owner.BreakRepairFragment;
import com.qin.fragment.main.owner.PlateForecastFragment;
import com.qin.fragment.main.owner.VINAnalysisFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/7 0007.
 */

public class OwnerServiceActivity extends AppCompatActivity {
    @BindView(R.id.fl_owner)
    FrameLayout flOwner;
    private int number;
    private FragmentTransaction mBeginTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerservice);
        ButterKnife.bind(this);
        number = getIntent().getExtras().getInt(ConstantValues.OWNERNUMBER);
        if (number == 1) {
            showFragment(new BreakRuleQueryFragment());
            return;
        }
        if (number == 2) {
            showFragment(new BreakRuleHappenFragment());
            return;
        }
        if (number == 3) {
            showFragment(new EndCodeLimitedFragment());
            return;
        }
        if (number == 4) {
            showFragment(new VINAnalysisFragment());
            return;
        }
        if (number == 5) {
            showFragment(new CarTypeFragment());
            return;
        }
        if (number == 6) {
            showFragment(new BreakRepairFragment());
            return;
        }
        if (number == 7) {
            showFragment(new BreakDTCFragment());
            return;
        }
        if (number == 8) {
            showFragment(new BreakAlarmFragment());
            return;
        }
        if (number == 9) {
            showFragment(new PlateForecastFragment());
            return;
        }
    }
    public void showFragment(Fragment fragment) {
//        Bundle bundle = new Bundle();
//        bundle.putString(ConstantValues.OWNERTOFRAGMENT, fragmentName);
//        fragment.setArguments(bundle);
        mBeginTransaction = getSupportFragmentManager().beginTransaction();
        mBeginTransaction.replace(R.id.fl_owner, fragment);
        mBeginTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
