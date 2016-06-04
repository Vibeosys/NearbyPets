package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nearbypets.R;
import com.nearbypets.utils.CustomVolleyRequestQueue;

public class ImageActivity extends AppCompatActivity {

    RelativeLayout imgImage;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        String imageName = getIntent().getExtras().getString("image");
        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
        int imgResId = getResources().getIdentifier(imageName, "drawable", "com.nearbypets");
        //imgImage.setBackgroundResource(imgResId);

        mImageLoader = CustomVolleyRequestQueue.getInstance(getApplicationContext())
                .getImageLoader();
       /* int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.nearbypets");
        imageView.setImageResource(imgResId);*/
        final String url = imageName;
        if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(imageView,
                        R.drawable.default_pet_image, R.drawable.default_pet_image));
                imageView.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.default_pet_image);
            }
        } else {
            imageView.setImageResource(R.drawable.default_pet_image);
        }
    }
}
