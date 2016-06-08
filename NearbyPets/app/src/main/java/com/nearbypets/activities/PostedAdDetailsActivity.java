package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nearbypets.R;
import com.nearbypets.data.downloaddto.ErrorDbDTO;

public class PostedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_posted_ad_details);

        btnAddToFav.setVisibility(View.GONE);
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }
}
