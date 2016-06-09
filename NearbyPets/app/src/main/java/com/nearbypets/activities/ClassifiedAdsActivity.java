package com.nearbypets.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.FacebookRequestError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.ClassifiedDbDTO;
import com.nearbypets.data.HiddenAdDbDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.SoldandDisableDbDTO;
import com.nearbypets.data.SortDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadProductDbDataDTO;
import com.nearbypets.data.downloaddto.DownloadRegisterDbDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.NotificationDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedAdsActivity extends ProductListActivity implements
        ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {
    private static final int REQ_TOKAN_HIDE_AD = 2;
    private static int storedPageNO = 0;
    private int mCategoryId;
    GPSTracker gpsTracker;
    private final int REQ_TOKEN_LIST = 1;
    private final int REQ_TOKEN_POST_HIDDEN_AD=34;
    private int adDisplay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_list);
        setTitle("Classified Ads");
        mCategoryId = getIntent().getIntExtra(AppConstants.CATEGORY_ID, 0);
        gpsTracker = new GPSTracker(getApplicationContext());
        //spnSortBy.setVisibility(View.GONE);
        storedPageNO = 0;
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        mProductAdapter.setCustomHideListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        spnSortBy.setOnItemSelectedListener(this);
        mProductAdapter.clear();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchList(1, mSortOption, sort);
                                    }
                                }
        );
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort);
            }
        });

    }

    private void fetchList(int pageNo, int sortOption, String sort) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            storedPageNO = pageNo;
            ClassifiedDbDTO productListDbDTO = new ClassifiedDbDTO(gpsTracker.getLatitude(), gpsTracker.getLongitude(), sortOption, sort, pageNo, mCategoryId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(productListDbDTO);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CLASSIFIED_AD, serializedJsonString);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "Error " + error.toString());
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_LIST:
                if (errorDbDTO.getErrorCode() != 0) {
                    Snackbar.make(getCurrentFocus(), "No more ads found", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case REQ_TOKAN_HIDE_AD:
                if (errorDbDTO.getErrorCode() == 0) {
                    Toast.makeText(getApplicationContext(), errorDbDTO.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    createAlertDialog("Error", "" + errorDbDTO.getMessage());
                }
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        updateSettings(settings);
        switch (requestToken) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "data" + data);
                ArrayList<ProductDbDTO> productDbDTOs = ProductDbDTO.deserializeToArray(data);
                updateList(productDbDTOs);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case REQ_TOKAN_HIDE_AD:
                Toast.makeText(getApplicationContext(), "Hide ad successfully", Toast.LENGTH_SHORT).show();
                break;
            case REQ_TOKEN_POST_HIDDEN_AD:
                Toast.makeText(getApplicationContext(), "Hide ad successfully", Toast.LENGTH_SHORT).show();
                Intent categoryList = new Intent(getApplicationContext(),CategoryListActivity.class );
                startActivity(categoryList);
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

    private void updateList(ArrayList<ProductDbDTO> data) {
        //mProductAdapter.clear();
        int id = Integer.parseInt(settingMap.get("FacebookAdPageSize"));
        ProDbDtoTOProDTO converter = new ProDbDtoTOProDTO(data);
        ArrayList<ProductDataDTO> productDataDTOs = converter.getProductDTOs();
        for (int i = 0; i < productDataDTOs.size(); i++) {
            adDisplay = adDisplay + 1;
            mProductAdapter.addItem(productDataDTOs.get(i));
            if ((adDisplay % id) == 0) {
                mProductAdapter.addSectionHeaderItem(productDataDTOs.get(i));
            }
        }
        //
        //mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mProductAdapter.clear();
        storedPageNO = 0;
        adDisplay = 0;
        fetchList(1, mSortOption, sort);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort);
            }
        });
    }

    @Override
    public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData) {
        productData.setFavouriteFlag(!value);
        mProductAdapter.notifyDataSetChanged();
        Log.i("TAG", "## imageClick" + value);
    }

    @Override
    public void onItemClickListener(int position, ProductDataDTO productData) {
        //Intent
        Log.i("TAG", "## Call To intent");
        ProductDataDTO productDataDTO = mProductAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), ClassfiedProductDescActivity.class);
        intent.putExtra(AppConstants.PRODUCT_DISTANCE, "" + productDataDTO.getDistance());
        intent.putExtra(AppConstants.PRODUCT_AD_ID, productData.getAdId());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SortDTO sortDTO = (SortDTO) mSortAdapter.getItem(position);
        mSortOption = sortDTO.getValue();
        sort = sortDTO.getSorting();
        swipeRefreshLayout.setRefreshing(true);
        mProductAdapter.clear();
        fetchList(1, mSortOption, sort);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onHideClickListener(int position, ProductDataDTO productData) {
        // showProgress(true, formView, progressBar);
        /*SoldandDisableDbDTO soldAndDisableDbDTO = new SoldandDisableDbDTO(productData.getAdId(), AppConstants.HIDE_AD);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(soldAndDisableDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.HIDDIN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKAN_HIDE_AD, tableDataDTO);*/
        HiddenAdDbDTO hiddenAdDbDTO = new HiddenAdDbDTO(productData.getAdId(),Integer.parseInt(AppConstants.HIDE_AD_ADMIN));
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(hiddenAdDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.HIDDIN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_POST_HIDDEN_AD, tableDataDTO);
    }


}
