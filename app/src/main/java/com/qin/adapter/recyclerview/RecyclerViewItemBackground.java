package com.qin.adapter.recyclerview;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class RecyclerViewItemBackground {
    private View mItemView;

    public RecyclerViewItemBackground(View itemView) {
        mItemView = itemView;
    }

    public void setRecyclerViewItemBackground() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mItemView.getContext().getTheme();
        int top = mItemView.getPaddingTop();
        int bottom = mItemView.getPaddingBottom();
        int left = mItemView.getPaddingLeft();
        int right = mItemView.getPaddingRight();
        if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
            mItemView.setBackgroundResource(typedValue.resourceId);
        }
        mItemView.setPadding(left, top, right, bottom);
    }
}
