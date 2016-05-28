package com.nearbypets.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by akshay on 10-03-2016.
 */
public class AbelRegularTextView extends TextView {
    public AbelRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Abel-Regular.ttf"));
    }
}
