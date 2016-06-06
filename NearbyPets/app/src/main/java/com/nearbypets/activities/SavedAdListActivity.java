package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nearbypets.R;
import com.nearbypets.utils.AppConstants;

public class SavedAdListActivity extends ProductListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_saved_ad_list);

        setTitle("Saved Ads");
        spnSortBy.setVisibility(View.GONE);
        mProductAdapter.setActivityFlag(AppConstants.POSTED_AD_FLAG_ADAPTER);
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
    }
}
