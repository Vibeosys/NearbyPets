package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nearbypets.R;

public class SavedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_saved_ad_details);
        setTitle("Saved Ad");
        btnSoldOut.setVisibility(View.GONE);
        btnDisable.setVisibility(View.GONE);
        btnAddToFav.setVisibility(View.GONE);

    }
}
