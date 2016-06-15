package com.nearbypets.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.adapters.ImageFragmentPagerAdapter;
import com.nearbypets.data.GetProductDescDbDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.SoldandDisableDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.ProductDescDbDTO;
import com.nearbypets.data.downloaddto.SaveAnAdDbDTO;
import com.nearbypets.fragments.SwipeFragment;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.DateUtils;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.views.RobotoItalicTextView;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import java.util.ArrayList;
import java.util.List;

public class ProductDescActivity extends BaseActivity implements SwipeFragment.CustomCall,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    //static final int NUM_ITEMS = 6;
    protected ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    protected RobotoMediumTextView mTxtProductTitle, mTxtProductPrice, mTxtAddressType;
    protected RobotoRegularTextView mTxtProductDesc, mTxtSellerName, mTxtSellerPh, mTxtSellerEmail,
            mTxtAdded, mTxtViews, mTxtDistance, mTxtAddress;
    RobotoItalicTextView txtShowOnMap;
    RadioButton radioButton, radioButton1, radioButton2;
    ViewPager viewPager;
    protected static ArrayList<String> mImageArray = new ArrayList<>();
    protected final int REQ_TOKAN_DESC = 1;
    protected final int REQ_TOKAN_SAVE_AD = 2;
    protected final int REQ_TOKEN_DISABLE = 3;
    protected final int REQ_TOKEN_SOLD_OUT = 4;
    protected View formView;
    protected View progressBar;
    //private int mScreenFlag;
    protected Button btnAddToFav, btnSoldOut, btnDisable;
    protected double mDistance;
    protected String mAdID;
    protected String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_desc);
        setTitle(getResources().getString(R.string.activity_product_desc));
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check network connection");

        } else {

            imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), mImageArray);
            SwipeFragment.setCustomButtonListner(this);
            /*try {*/
               /* String distance =
                if (distance == null || TextUtils.isEmpty(distance)) {

                } else*/
            mDistance = getIntent().getDoubleExtra(AppConstants.PRODUCT_DISTANCE, 0);
            /*} catch (Exception e) {
                Log.e("TAG", "ERROR IN PRODUCT DESC DISTANCE");
            }*/
            try {
                mAdID = getIntent().getExtras().getString(AppConstants.PRODUCT_AD_ID);
            } catch (Exception e) {
                Log.e("TAG", "ERROR IN PRODUCT DESC AD ID");
            }
            setUpUI();
            callToDesc();

            viewPager.setAdapter(imageFragmentPagerAdapter);
        }

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
        txtShowOnMap = (RobotoItalicTextView) findViewById(R.id.txtShowOnMap);
        mTxtAddressType = (RobotoMediumTextView) findViewById(R.id.txtAdressType);
        mTxtAddress = (RobotoRegularTextView) findViewById(R.id.txtAddress);
        formView = findViewById(R.id.fromProductDesc);
        progressBar = findViewById(R.id.progressBar);
        btnAddToFav = (Button) findViewById(R.id.btnAddToFav);
        btnSoldOut = (Button) findViewById(R.id.btnSoldOut);
        btnDisable = (Button) findViewById(R.id.btnDisable);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton3);
        btnAddToFav.setVisibility(View.VISIBLE);
        btnAddToFav.setOnClickListener(this);
        btnSoldOut.setOnClickListener(this);
        btnDisable.setOnClickListener(this);
        mTxtSellerPh.setOnClickListener(this);
        txtShowOnMap.setOnClickListener(this);
        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        AdView adView = new AdView(getApplicationContext(), "1715459422041023_1722420624678236", AdSize.BANNER_320_50);
        adViewContainer.addView(adView);
        AdSettings.addTestDevice("HASHED ID");
        adView.loadAd();

        RelativeLayout adViewContainer1 = (RelativeLayout) findViewById(R.id.adViewContainer1);
        AdView adView1 = new AdView(getApplicationContext(), "1715459422041023_1722420858011546", AdSize.BANNER_320_50);
        adViewContainer1.addView(adView1);
        AdSettings.addTestDevice("HASHED ID");
        adView1.loadAd();

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
       /* switch (mScreenFlag) {
            case AppConstants.VIEW_AD_DETAILS_SCREEN:
                btnDisable.setVisibility(View.GONE);
                btnSoldOut.setVisibility(View.GONE);
                break;
            default:
                break;
        }*/
    }

    private void callToDesc() {
        showProgress(true, formView, progressBar);
        GetProductDescDbDTO productListDbDTO = new GetProductDescDbDTO(mAdID);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(productListDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PRODUCT_DESC, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_DESC, tableDataDTO);
    }

    protected void callToMap() {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        finish();

    }

    private void callToDialer() {
        getPermissionsForPhoneCall();
    }

    @Override
    protected void callToPhone() {
        String posted_by = mTxtSellerPh.getText().toString();
        if (TextUtils.isEmpty(posted_by) || posted_by == null) {
            createAlertDialog("Call Error", "We could not connect you to the call");
        } else {

            String uri = "tel:" + posted_by.trim();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedAdId", mAdID);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdID = savedInstanceState.getString("savedAdId");
    }

    @Override
    public void onButtonClickListener(int id, int position) {
        Intent i = new Intent(getApplicationContext(), ImageActivity.class);
        i.putExtra("image", mImageArray.get(position));
        startActivity(i);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKAN_DESC:
                showProgress(false, formView, progressBar);
                //Toast.makeText("No more ads", "There are no more ads t")
                //createAlertDialog("Error", error.getMessage());
                Log.i(TAG, "##" + error.toString());
                break;
            case REQ_TOKEN_DISABLE:
                showProgress(false, formView, progressBar);
                //createAlertDialog("Error", error.getMessage());
                Log.i(TAG, "##" + error.toString());
                break;
            case REQ_TOKEN_SOLD_OUT:
                showProgress(false, formView, progressBar);
                //createAlertDialog("Error", error.getMessage());
                Log.i(TAG, "##" + error.toString());
                break;
            case REQ_TOKAN_SAVE_AD:
                showProgress(false, formView, progressBar);
                //createAlertDialog("Error", error.getMessage());
                Log.i(TAG, "##" + error.toString());
                break;

        }
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        showProgress(false, formView, progressBar);
        switch (requestToken) {
            case REQ_TOKAN_DESC:
                if (errorDbDTO.getErrorCode() != 0) {
                    //createAlertDialog("Error", "" + errorDbDTO.getMessage());
                    Log.i("TAG", "##" + errorDbDTO.getMessage());
                }

                break;
            case REQ_TOKAN_SAVE_AD:
                if (errorDbDTO.getErrorCode() == 0) {
                    Log.i("TAG", "##" + errorDbDTO.getMessage());
                    Toast.makeText(getApplicationContext(), "" + errorDbDTO.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //createAlertDialog("Error", "" + errorDbDTO.getMessage());
                    Log.i("TAG", "##" + errorDbDTO.getMessage());
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.e(TAG, "## error on response" + data);
        showProgress(false, formView, progressBar);
        switch (requestToken) {
            case REQ_TOKAN_DESC:

                showProgress(false, formView, progressBar);
                try {
                    ProductDescDbDTO productDescDbDTO = ProductDescDbDTO.deserializeJson(data);
                    updateUI(productDescDbDTO);
                    Log.i(TAG, productDescDbDTO.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                Log.i("TAG", "data" + data);
                break;
            case REQ_TOKAN_SAVE_AD:
                Toast.makeText(getApplicationContext(), "Ad is added to your favorites", Toast.LENGTH_SHORT).show();
                break;
            case REQ_TOKEN_DISABLE:
                Toast.makeText(getApplicationContext(), "Ad is now Disabled", Toast.LENGTH_SHORT).show();
                break;
            case REQ_TOKEN_SOLD_OUT:
                Toast.makeText(getApplicationContext(), "Ad is now set to SOLD OUT", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        //updateSettings(settings);
    }


    private void updateUI(ProductDescDbDTO product) {

        mTxtProductTitle.setText(product.getAdTitle());
        mTxtProductPrice.setText(getResources().getString(R.string.str_euro_price_symbol) + " "
                + String.format("%.2f", product.getPrice()));
        mTxtProductDesc.setText(product.getDescription());
        mTxtSellerName.setText(product.getName());
        mTxtSellerPh.setText(product.getPhone());
        mTxtSellerEmail.setText(product.getEmail());
        DateUtils date = new DateUtils();
        mTxtAdded.setText(date.getLocalDateInFormat(product.getPostedDt()));
        mTxtViews.setText("" + product.getAdViews());
        mTxtDistance.setText(String.format("%.2f", mDistance)
                + " kilometers away from you.");
        mAddress = product.getAdAddress();
        if (product.getIsAddress() == 1) {
            mTxtAddressType.setText("City:");
        } else {
            mTxtAddressType.setText("Full Address:");
        }
        mTxtAddress.setText(product.getDisplayAddress());
        ArrayList<String> images = product.getImages();
        mImageArray.clear();
        for (int j = 0; j < images.size(); j++) {
            mImageArray.add(images.get(j));
        }
        switch (images.size()) {
            case 0:
                radioButton.setVisibility(View.GONE);
                radioButton1.setVisibility(View.GONE);
                radioButton2.setVisibility(View.GONE);
            case 1:
                radioButton.setVisibility(View.VISIBLE);
                radioButton1.setVisibility(View.GONE);
                radioButton2.setVisibility(View.GONE);
                break;
            case 2:
                radioButton.setVisibility(View.VISIBLE);
                radioButton1.setVisibility(View.VISIBLE);
                radioButton2.setVisibility(View.GONE);
                break;
            case 3:
                radioButton.setVisibility(View.VISIBLE);
                radioButton1.setVisibility(View.VISIBLE);
                radioButton2.setVisibility(View.VISIBLE);
                break;
        }
        imageFragmentPagerAdapter.notifyDataSetChanged();
        if (product.getEmail().equals(mSessionManager.getUserEmailId())) {
            btnSoldOut.setVisibility(View.VISIBLE);
            btnDisable.setVisibility(View.VISIBLE);
        } else {
            btnSoldOut.setVisibility(View.GONE);
            btnDisable.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAddToFav:
                callToSaveAd();
                break;
            case R.id.btnDisable:
                callToAlertBox(R.id.btnDisable, "Confirm Disable", "Are you sure to disable this ad?");

                break;
            case R.id.btnSoldOut:
                callToAlertBox(R.id.btnSoldOut, "Confirm Sold Out", "Are you sure to sold out this ad?");
                break;
            case R.id.txtSellerPh:
                callToDialer();
                break;
            case R.id.txtShowOnMap:
                callToMap();
                break;
        }
    }

    private void callToAlertBox(final int btn, String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // whatever...
                        switch (btn) {
                            case R.id.btnDisable:
                                callToDisableAd();
                                break;
                            case R.id.btnSoldOut:
                                callToSoldOutAd();
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }

    private void callToSoldOutAd() {
        showProgress(true, formView, progressBar);
        SoldandDisableDbDTO soldAndDisableDbDTO = new SoldandDisableDbDTO(mAdID);
        //Gson gson = new Gson();
        String serializedJsonString = soldAndDisableDbDTO.serializeString();
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SOLD_OUT_POST_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_SOLD_OUT, tableDataDTO);
    }

    private void callToDisableAd() {
        showProgress(true, formView, progressBar);
        SoldandDisableDbDTO soldAndDisableDbDTO = new SoldandDisableDbDTO(mAdID);
        //Gson gson = new Gson();
        String serializedJsonString = soldAndDisableDbDTO.serializeString();
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.DISABLE_POST_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_DISABLE, tableDataDTO);
    }

    private void callToSaveAd() {
        showProgress(true, formView, progressBar);
        SaveAnAdDbDTO saveAnAdDbDTO = new SaveAnAdDbDTO(mAdID, mSessionManager.getUserId());
        //Gson gson = new Gson();
        String serializedJsonString = saveAnAdDbDTO.serializeString();
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SAVE_AN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_SAVE_AD, tableDataDTO);
    }

}
