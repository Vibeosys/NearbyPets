package com.nearbypets.activities;

import android.os.Bundle;
import android.view.View;

public class SavedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_saved_ad_details);
        setTitle("Favorite Ad");
        btnSoldOut.setVisibility(View.GONE);
        btnDisable.setVisibility(View.GONE);
        btnAddToFav.setVisibility(View.GONE);

    }
}
