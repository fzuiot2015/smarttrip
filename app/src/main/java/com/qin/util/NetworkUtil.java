package com.qin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class NetworkUtil {
    public static final int TYPE_NONE = -1;
    public static final int TYPE_SUCCESS = 0;

    private NetworkUtil() {}
    /**
     * 获取网络状态
     */
    public static int getNetWorkStates(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()&& activeNetworkInfo.isAvailable()) {
            return TYPE_SUCCESS;
        }else{
            return TYPE_NONE;
        }

    }
}
