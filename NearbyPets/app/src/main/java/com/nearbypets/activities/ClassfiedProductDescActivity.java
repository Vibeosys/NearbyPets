package com.nearbypets.activities;

import android.os.Bundle;
import android.view.View;

import com.nearbypets.utils.AppConstants;

public class ClassfiedProductDescActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_classfied_product_desc);
        
        if (mSessionManager.getUserRoleId() == AppConstants.ROLL_ID_ADMIN)
            btnAddToFav.setVisibility(View.GONE);
        else
            btnAddToFav.setVisibility(View.VISIBLE);

        btnDisable.setVisibility(View.GONE);
        btnSoldOut.setVisibility(View.GONE);
        mDistance = Double.parseDouble(getIntent().getExtras().getString(AppConstants.PRODUCT_DISTANCE));
        mAdID = getIntent().getExtras().getString(AppConstants.PRODUCT_AD_ID);

    }
}
