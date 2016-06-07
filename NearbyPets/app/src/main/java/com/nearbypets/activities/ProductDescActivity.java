package com.nearbypets.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.nearbypets.data.SoldandDisableDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadProductDecs;
import com.nearbypets.data.downloaddto.DownloadRegisterDbDTO;
import com.nearbypets.data.downloaddto.NotificationDTO;
import com.nearbypets.data.downloaddto.ProductDescDbDTO;
import com.nearbypets.data.downloaddto.SaveAnAdDbDTO;
import com.nearbypets.fragments.SwipeFragment;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.DateUtils;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDescActivity extends BaseActivity implements SwipeFragment.CustomCall,
        ServerSyncManager.OnStringResultReceived, ServerSyncManager.OnStringErrorReceived, View.OnClickListener {
    static final int NUM_ITEMS = 6;
    protected ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    protected RobotoMediumTextView mTxtProductTitle, mTxtProductPrice;
    protected RobotoRegularTextView mTxtProductDesc, mTxtSellerName, mTxtSellerPh, mTxtSellerEmail,
            mTxtAdded, mTxtViews, mTxtDistance;
    ViewPager viewPager;
    protected static ArrayList<String> mImageArray = new ArrayList<>();
    protected final int REQ_TOKAN_DESC = 1;
    protected final int REQ_TOKAN_SAVE_AD = 2;
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
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");

        } else {
            imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager(), mImageArray);
            SwipeFragment.setCustomButtonListner(this);
            try {
                mDistance = Double.parseDouble(getIntent().getExtras().getString(AppConstants.PRODUCT_DISTANCE));
            } catch (Exception e) {
                Log.e("TAG", "ERROR IN PRODUCT DESC DISTANCE");
            }
            try {
                mAdID = getIntent().getExtras().getString(AppConstants.PRODUCT_AD_ID);
            } catch (Exception e) {
                Log.e("TAG", "ERROR IN PRODUCT DESC AD ID");
            }
            setUpUI();
            callToDesc();
            mServerSyncManager.setOnStringResultReceived(this);
            mServerSyncManager.setOnStringErrorReceived(this);
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
        formView = findViewById(R.id.fromProductDesc);
        progressBar = findViewById(R.id.progressBar);
        btnAddToFav = (Button) findViewById(R.id.btnAddToFav);
        btnSoldOut = (Button) findViewById(R.id.btnSoldOut);
        btnDisable = (Button) findViewById(R.id.btnDisable);

        btnAddToFav.setOnClickListener(this);
        btnSoldOut.setOnClickListener(this);
        btnDisable.setOnClickListener(this);
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

    protected void callToMap(View v) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    protected void callToDialer(View v) {
        String posted_by = mTxtSellerPh.getText().toString();
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
            case REQ_TOKAN_SAVE_AD:
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
                    updateUI(downloadData.getData().get(0).getData());
                    Log.i(TAG, downloadData.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                Log.i("TAG", "data" + data);
                break;
            case REQ_TOKAN_SAVE_AD:
                showProgress(true, formView, progressBar);
                Log.d("RESULT", "##REQ" + data.toString());
                try {
                    DownloadRegisterDbDTO download = new Gson().fromJson(data.toString(), DownloadRegisterDbDTO.class);
                    updateSettings(download.getSettings());
                    Log.i(TAG, download.toString());
                    checkStatus(download.getData().get(0).getData());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                break;
        }
    }

    private void checkStatus(ArrayList<NotificationDTO> notificationDTOs) {

        NotificationDTO notificationDTO = notificationDTOs.get(0);
        if (notificationDTO.getErrorCode() == 0 || notificationDTO.getErrorCode() == 102) {
            Log.i("TAG", "##" + notificationDTO.getMessage());
            Toast.makeText(getApplicationContext(), "" + notificationDTO.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            createAlertDialog("Error", "" + notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
        }
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
        ArrayList<String> images = product.getImages();
        mImageArray.clear();
        for (int j = 0; j < images.size(); j++) {
            mImageArray.add(images.get(j));
        }
        imageFragmentPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnAddToFav:
                callToSaveAd();
                break;
            case R.id.btnDisable:
                callToDisableAd();
                break;
            case R.id.btnSoldOut:
                callToSoldOutAd();
                break;
        }
    }

    private void callToSoldOutAd() {
        showProgress(true, formView, progressBar);
        SoldandDisableDbDTO soldAndDisableDbDTO = new SoldandDisableDbDTO(mAdID, AppConstants.SOLD_AD);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(soldAndDisableDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SOLD_OUT_POST_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_SAVE_AD, tableDataDTO);
    }

    private void callToDisableAd() {
        showProgress(true, formView, progressBar);
        SoldandDisableDbDTO soldAndDisableDbDTO = new SoldandDisableDbDTO(mAdID, AppConstants.DISEABLE_AD);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(soldAndDisableDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.DISABLE_POST_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_SAVE_AD, tableDataDTO);

    }

    private void callToSaveAd() {
        showProgress(true, formView, progressBar);
        SaveAnAdDbDTO saveAnAdDbDTO = new SaveAnAdDbDTO(mAdID, mSessionManager.getUserId());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(saveAnAdDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SAVE_AN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_SAVE_AD, tableDataDTO);
    }
}
