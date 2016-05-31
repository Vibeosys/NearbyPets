package com.nearbypets.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.nearbypets.R;
import com.nearbypets.adapters.ImageFragmentPagerAdapter;
import com.nearbypets.fragments.SwipeFragment;

public class ProductDescActivity extends AppCompatActivity implements SwipeFragment.CustomCall {
    static final int NUM_ITEMS = 6;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    public static String[] IMAGE_NAME = {"sliderbird", "sliderreptile", "sliderdogs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);
        setTitle(getResources().getString(R.string.activity_product_desc));
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), IMAGE_NAME);
        SwipeFragment.setCustomButtonListner(this);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(imageFragmentPagerAdapter);
    }

    protected void callToMap(View v) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=1600 Amphitheatre Parkway, Mountain+View, California");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    protected void callToDialer(View v) {
        String posted_by = "123 456 789";
        String uri = "tel:" + posted_by.trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onButtonClickListener(int id, int position) {
        Intent i = new Intent(getApplicationContext(), ImageActivity.class);
        i.putExtra("image", IMAGE_NAME[position]);
        startActivity(i);
    }
}
