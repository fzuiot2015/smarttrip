package com.qin.util;

import android.app.Activity;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class ScreenUtils {
    /**
     * 获得屏幕宽/高
     */
    public static int getWindowHeight(Activity acitvity) {
        return acitvity.getWindowManager().getDefaultDisplay().getHeight();
    }

    public static int getWindowWidth(Activity acitvity) {
        return acitvity.getWindowManager().getDefaultDisplay().getWidth();
    }
}
