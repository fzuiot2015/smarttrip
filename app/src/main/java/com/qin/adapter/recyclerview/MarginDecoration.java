package com.qin.adapter.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by FanXia on 2016/5/9 0009.
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public MarginDecoration(int space){
        this.margin = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,margin,margin,0);
    }
}
