package com.qin.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qin.util.NetworkUtil;
import com.qin.util.ToastUtils;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {

    @Override

    public void onReceive(Context context, Intent intent) {
        int netWorkStates = NetworkUtil.getNetWorkStates(context);

        switch (netWorkStates) {
            case NetworkUtil.TYPE_NONE:
                //断网
                ToastUtils.showBgResource(context,"亲，您的网络出错啦！");
                break;
            case NetworkUtil.TYPE_SUCCESS:
                //移动网络
           //     ToastUtils.showBgResource(context,"有网络");
                break;
        }
    }
}
