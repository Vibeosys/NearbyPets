package com.nearbypets.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.nearbypets.utils.AppConstants;

public class PostedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_posted_ad_details);
        //btnAddToFav.setVisibility(View.GONE);


        try {
            String distance = getIntent().getStringExtra(AppConstants.PRODUCT_DISTANCE);
            if (distance == null || TextUtils.isEmpty(distance)) {

            } else
                mDistance = Double.parseDouble(distance);
        } catch (Exception e) {
            Log.e("TAG", "ERROR IN PRODUCT DESC DISTANCE");
        }
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

}
