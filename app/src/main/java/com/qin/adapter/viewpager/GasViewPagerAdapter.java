package com.qin.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.qin.fragment.BaseFragment;
import java.util.List;


/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class GasViewPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mDatas;
    public GasViewPagerAdapter(FragmentManager fm, List<BaseFragment> datas) {
        super(fm);
        this.mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
