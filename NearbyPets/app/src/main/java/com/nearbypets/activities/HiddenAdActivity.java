package com.nearbypets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.ArrayList;
import java.util.List;

public class HiddenAdActivity extends ProductListActivity implements
        ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    //GPSTracker gpsTracker;

    private static int storedPageNO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_hidden_ad);
        setTitle("Hidden Ads");


        getCurrentLocation(mLocationManager);
        //gpsTracker = new GPSTracker(getApplicationContext());
        spnSortBy.setVisibility(View.GONE);

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        storedPageNO = 0;
        mProductAdapter.setActivityFlag(AppConstants.POSTED_AD_FLAG_ADAPTER);
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        mProductAdapter.clear();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        //fetchList(1);
                                        fetchList(1);
                                        //logic to refersh list
                                    }
                                }
        );
        /*mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page);
            }
        });*/


    }


    private void fetchList(int pageNo) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check network connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {

            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.GET_HIDDEN_AD);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_GET_HIDDEN_LIST, tableDataDTO);
            /*PostedAdDbDTO productListDbDTO = new PostedAdDbDTO(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 0, "ASC", pageNo, mSessionManager.getUserId());
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(productListDbDTO);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.POSTED_AD, serializedJsonString);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);*/
        }
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_GET_HIDDEN_LIST:
                Log.i("TAG", "Error " + error.toString());
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    private void updateList(ArrayList<ProductDbDTO> data) {
        //mProductAdapter.clear();
        int id = Integer.parseInt(settingMap.get("FacebookAdPageSize"));
        ProDbDtoTOProDTO converter = new ProDbDtoTOProDTO(data);
        ArrayList<ProductDataDTO> productDataDTOs = converter.getProductDTOs();
        for (int i = 0; i < productDataDTOs.size(); i++) {
            mProductAdapter.addItem(productDataDTOs.get(i));
            if ((i % id) == 0) {
                mProductAdapter.addSectionHeaderItem(productDataDTOs.get(i));
            }
        }
    }

    @Override
    public void onRefresh() {
        mProductAdapter.clear();
        storedPageNO = 0;
        fetchList(1);
    }

    @Override
    public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData) {
        Toast.makeText(getApplicationContext(), "Ad is now hidden from clients", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClickListener(int position, ProductDataDTO productData) {
        //Intent
        Log.i("TAG", "## Call To intent");
        ProductDataDTO productDataDTO = mProductAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), PostedAdDetailsActivity.class);
        intent.putExtra(AppConstants.PRODUCT_DISTANCE, "" + productDataDTO.getDistance());
        intent.putExtra(AppConstants.PRODUCT_AD_ID, productData.getAdId());
        startActivity(intent);
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        //createAlertDialog("Login error", "" + errorDbDTO.getMessage());
        Log.i("TAG", "##" + errorDbDTO.getMessage());
    }


    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        //super.onResultReceived(data,settings, requestToken);
        switch (requestToken) {
            case REQ_TOKEN_GET_HIDDEN_LIST:
                Log.i("TAG", "data" + data);
                try {
                    ArrayList<ProductDbDTO> downloadProductDbDataDTO = ProductDbDTO.deserializeToArray(data);

                    updateList(downloadProductDbDataDTO);
                    Log.i(TAG, downloadProductDbDataDTO.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                swipeRefreshLayout.setRefreshing(false);
                break;
            case REQ_TOKEN_POST_HIDDEN_AD:
                Log.i("TAG", "data" + data);
                break;
        }
        updateSettings(settings);
    }

}
