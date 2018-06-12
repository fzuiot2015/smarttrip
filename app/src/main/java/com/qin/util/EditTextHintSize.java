package com.qin.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class EditTextHintSize {

    public static void setEditTextHintSize(Context context, EditText et, int size,String text) {
        String hintStr = text;
        SpannableString ss =  new SpannableString(hintStr);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setHint(new SpannedString(ss));
    }
    public static void setAutoEditTextHintSize(Context context, AutoCompleteTextView et, int size, String text) {
        String hintStr = text;
        SpannableString ss =  new SpannableString(hintStr);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setHint(new SpannedString(ss));
    }
}
