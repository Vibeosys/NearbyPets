package com.nearbypets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nearbypets.R;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SavedAdListDbDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.ArrayList;
import java.util.List;

public class SavedAdListActivity extends ProductListActivity
        implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    //GPSTracker gpsTracker;
    private static int storedPageNO = 0;
    private static String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_saved_ad_list);
        setTitle(getResources().getString(R.string.nav_opt_my_saved_ads));
        getCurrentLocation(mLocationManager);
        //gpsTracker = new GPSTracker(getApplicationContext());
        spnSortBy.setVisibility(View.GONE);
        mServerSyncManager.setOnStringErrorReceived(this);
        storedPageNO = 0;
        mServerSyncManager.setOnStringResultReceived(this);
        mProductAdapter.setActivityFlag(AppConstants.FAVORITE_AD_FLAG_ADAPTER);
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
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchList(page);
                return true;
            }
        });
    }

    private void fetchList(int pageNo) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            storedPageNO = pageNo;
            //PostedAdDbDTO productListDbDTO = new PostedAdDbDTO(currentLat, currentLong, 0, "ASC", pageNo, mSessionManager.getUserId(),searchText);
            SavedAdListDbDTO productListDbDTO = new SavedAdListDbDTO(currentLat, currentLong, 0, "ASC", pageNo, mSessionManager.getUserId(), searchText);
            String serializedJsonString = productListDbDTO.serializeString();
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SAVED_AD, serializedJsonString);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);
            mProductAdapter.notifyDataSetChanged();
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
        //super.onDataErrorReceived(errorDbDTO, requestToken);
        if (errorDbDTO.getErrorCode() != 0) {
            //createAlertDialog("Error", errorDbDTO.getMessage());
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        //super.onResultReceived(data, settings, requestToken);

        updateSettings(settings);
        switch (requestToken) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "data" + data);
                ArrayList<ProductDbDTO> productDbDTOs = ProductDbDTO.deserializeToArray(data);
                updateList(productDbDTOs);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case REQ_REMOVE_SAVED_AD:
                Log.i("TAG", "data" + data);
                onRefresh();
                Toast.makeText(getApplicationContext(),"Ad removed from your favorites",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateList(ArrayList<ProductDbDTO> data) {
        mListViewProduct.setVisibility(View.VISIBLE);
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
        mListViewProduct.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        storedPageNO = 0;
        mProductAdapter.clear();
        fetchList(1);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchList(page);
                return true;
            }
        });
        mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData) {
        productData.setFavouriteFlag(!value);
        callToRemoveSavedAd(productData.getAdId());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.classified_ad_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.clasified_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(getApplicationContext()," "+query,Toast.LENGTH_LONG).show();
                searchText = query;
                onRefresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchText = "";
                //sendSearchData("");
                // onRefresh();
                //fetchList(1, mSortOption, sort, searchText);
                onRefresh();
                // Toast.makeText(getApplicationContext(),"Close button is clicked",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return true;
    }

}
