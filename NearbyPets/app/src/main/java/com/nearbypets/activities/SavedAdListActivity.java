package com.nearbypets.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.GetSavedAd;
import com.nearbypets.data.PostedAdDbDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.ProductListDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadProductDbDataDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class SavedAdListActivity extends ProductListActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    GPSTracker gpsTracker;
    private final int REQ_TOKEN_LIST = 1;
    private static int storedPageNO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_saved_ad_list);
        setTitle("Saved Ads");
        gpsTracker = new GPSTracker(getApplicationContext());
        spnSortBy.setVisibility(View.GONE);
        mServerSyncManager.setOnStringErrorReceived(this);
        storedPageNO = 0;
        mServerSyncManager.setOnStringResultReceived(this);
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
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page);
            }
        });

    }

    private void fetchList(int pageNo) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            PostedAdDbDTO productListDbDTO = new PostedAdDbDTO(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 0, "ASC", pageNo, mSessionManager.getUserId());
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(productListDbDTO);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.POSTED_AD, serializedJsonString);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);
        }
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "Error " + error.toString());
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {

        switch (requestTokan) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "data" + data);
                try {
                    DownloadProductDbDataDTO downloadProductDbDataDTO = new Gson().fromJson(data.toString(), DownloadProductDbDataDTO.class);
                    updateSettings(downloadProductDbDataDTO.getSettings());
                    updateList(downloadProductDbDataDTO.getData());
                    Log.i(TAG, downloadProductDbDataDTO.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
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
        //
        //mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        storedPageNO = 0;
        mProductAdapter.clear();
        fetchList(1);
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
        Intent intent = new Intent(getApplicationContext(), SavedAdDetailsActivity.class);
        intent.putExtra(AppConstants.PRODUCT_DISTANCE, "" + productDataDTO.getDistance());
        intent.putExtra(AppConstants.PRODUCT_AD_ID, productData.getAdId());
        startActivity(intent);
    }
}