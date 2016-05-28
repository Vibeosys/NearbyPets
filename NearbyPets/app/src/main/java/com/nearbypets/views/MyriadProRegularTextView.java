package com.nearbypets.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by akshay on 28-05-2016.
 */
public class MyriadProRegularTextView extends TextView {

    public MyriadProRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Regular.ttf"));
    }
}
