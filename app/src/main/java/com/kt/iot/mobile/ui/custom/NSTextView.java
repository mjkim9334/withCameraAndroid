package com.kt.iot.mobile.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kt.iot.mobile.GiGaIotApplication;

/**
 * Created by ceoko on 15. 3. 30..
 */

/**
 * NotoSansKR Font가 적용된 TextView
 */
public class NSTextView extends TextView {

    public NSTextView(Context context) {
        super(context);
        initFont();
    }

    public NSTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public NSTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NSTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFont();
    }

    private void initFont(){
        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/NotoSansKR-Thin.otf");
        Typeface tf = GiGaIotApplication.getDefaultTypeFace();
        setTypeface(tf);
        setIncludeFontPadding(false);
    }

}
