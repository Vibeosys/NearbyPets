package com.nearbypets.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by shrinivas on 30-05-2016.
 */
public class MyriadProRegularEditTextView extends EditText {
    public MyriadProRegularEditTextView(Context context,AttributeSet attrs) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/MyriadPro-Regular.otf"));
    }
}
