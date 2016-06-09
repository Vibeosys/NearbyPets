package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class PostedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_posted_ad_details);
        btnAddToFav.setVisibility(View.GONE);


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
