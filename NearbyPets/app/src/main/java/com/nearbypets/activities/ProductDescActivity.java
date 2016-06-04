package com.nearbypets.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.adapters.ImageFragmentPagerAdapter;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadProductDecs;
import com.nearbypets.data.downloaddto.ProductDescDbDTO;
import com.nearbypets.data.downloaddto.ProductImagesDbDTO;
import com.nearbypets.fragments.SwipeFragment;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDescActivity extends BaseActivity implements SwipeFragment.CustomCall,
        ServerSyncManager.OnStringResultReceived, ServerSyncManager.OnStringErrorReceived {
    static final int NUM_ITEMS = 6;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    private RobotoMediumTextView mTxtProductTitle, mTxtProductPrice;
    private RobotoRegularTextView mTxtProductDesc, mTxtSellerName, mTxtSellerPh, mTxtSellerEmail,
            mTxtAdded, mTxtViews, mTxtDistance;
    ViewPager viewPager;
    private static ArrayList<String> mImageArray = new ArrayList<>();
    private final int REQ_TOKAN_DESC = 1;
    private View formView;
    private View progressBar;
    private int mScreenFlag;
    private Button btnAddToFav, btnSoldOut, btnDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);
        setTitle(getResources().getString(R.string.activity_product_desc));
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), mImageArray);
        SwipeFragment.setCustomButtonListner(this);
        mScreenFlag = getIntent().getExtras().getInt(AppConstants.PRODUCT_DESC_FLAG);

        setUpUI();
        callToDesc();
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        viewPager.setAdapter(imageFragmentPagerAdapter);
    }

    private void setUpUI() {
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        viewPager = (ViewPager) findViewById(R.id.pager);
        mImageArray.add("sliderbird");
        mTxtProductTitle = (RobotoMediumTextView) findViewById(R.id.txtProductTitle);
        mTxtProductPrice = (RobotoMediumTextView) findViewById(R.id.txtProductPrice);
        mTxtProductDesc = (RobotoRegularTextView) findViewById(R.id.txtProductDesc);
        mTxtSellerName = (RobotoRegularTextView) findViewById(R.id.txtSellerName);
        mTxtSellerPh = (RobotoRegularTextView) findViewById(R.id.txtSellerPh);
        mTxtSellerEmail = (RobotoRegularTextView) findViewById(R.id.txtSellerEmail);
        mTxtAdded = (RobotoRegularTextView) findViewById(R.id.txtAdded);
        mTxtViews = (RobotoRegularTextView) findViewById(R.id.txtViews);
        mTxtDistance = (RobotoRegularTextView) findViewById(R.id.txtDistance);
        formView = findViewById(R.id.fromProductDesc);
        progressBar = findViewById(R.id.progressBar);
        btnAddToFav = (Button) findViewById(R.id.btnAddToFav);
        btnSoldOut = (Button) findViewById(R.id.btnSoldOut);
        btnDisable = (Button) findViewById(R.id.btnDisable);
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
        switch (mScreenFlag) {
            case AppConstants.VIEW_AD_DETAILS_SCREEN:
                btnDisable.setVisibility(View.GONE);
                btnSoldOut.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void callToDesc() {
        showProgress(true, formView, progressBar);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PRODUCT_DESC);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_DESC, tableDataDTO);
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
        i.putExtra("image", mImageArray.get(position));
        startActivity(i);
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKAN_DESC:
                showProgress(false, formView, progressBar);
                createAlertDialog("Error", error.getMessage());
                Log.i(TAG, "##" + error.toString());
                break;
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKAN_DESC:
                showProgress(false, formView, progressBar);
                try {
                    DownloadProductDecs downloadData = new Gson().fromJson(data.toString(), DownloadProductDecs.class);
                    updateSettings(downloadData.getSettings());
                    updateUI(downloadData.getData());
                    Log.i(TAG, downloadData.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                Log.i("TAG", "data" + data);
                break;
        }
    }

    private void updateUI(ArrayList<ProductDescDbDTO> data) {
        for (ProductDescDbDTO product : data) {
            mTxtProductTitle.setText(product.getPet().getTitle());
            mTxtProductPrice.setText(getResources().getString(R.string.str_euro_price_symbol) + " "
                    + String.format("%.0f", product.getPet().getPrice()));
            mTxtProductDesc.setText(product.getPet().getDescription());
            mTxtSellerName.setText(product.getSeller().getName());
            mTxtSellerPh.setText(product.getSeller().getPhone());
            mTxtSellerEmail.setText(product.getSeller().getEmail());
            mTxtAdded.setText(product.getDetails().getDate());
            mTxtViews.setText("" + product.getDetails().getViews());
            mTxtDistance.setText(String.format("%.0f", product.getDetails().getDistance())
                    + " kilometers away from you.");
            ArrayList<ProductImagesDbDTO> images = product.getImages();
            mImageArray.clear();
            for (int j = 0; j < images.size(); j++) {
                mImageArray.add(images.get(j).getUrl());
            }
            imageFragmentPagerAdapter.notifyDataSetChanged();
        }
    }


}
