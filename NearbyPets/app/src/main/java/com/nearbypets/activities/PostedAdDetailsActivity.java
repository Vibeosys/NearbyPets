package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;

import java.util.ArrayList;
import java.util.List;

public class PostedAdDetailsActivity extends ProductDescActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_posted_ad_details);
        btnAddToFav.setVisibility(View.GONE);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

}
