package com.qin.adapter.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class MarginDecorationVertical extends RecyclerView.ItemDecoration {

    private int space;

    public MarginDecorationVertical(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != 0)
            outRect.top = space;
    }
}
