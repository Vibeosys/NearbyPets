package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nearbypets.R;

public class ImageActivity extends AppCompatActivity {

    RelativeLayout imgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String imageName = getIntent().getExtras().getString("image");
        imgImage = (RelativeLayout) findViewById(R.id.imageLayout);
        int imgResId = getResources().getIdentifier(imageName, "drawable", "com.nearbypets");
        imgImage.setBackgroundResource(imgResId);
    }
}
