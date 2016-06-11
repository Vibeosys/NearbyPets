package com.nearbypets.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.PostedAdDbDTO;
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
    private String searchText;
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
                                        fetchList(1,"");
                                        //logic to refersh list
                                    }
                                }
        );
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page,"");
            }
        });
    }

    private void fetchList(int pageNo,String searchText) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            storedPageNO = pageNo;
            //PostedAdDbDTO productListDbDTO = new PostedAdDbDTO(currentLat, currentLong, 0, "ASC", pageNo, mSessionManager.getUserId(),searchText);
            SavedAdListDbDTO  productListDbDTO = new SavedAdListDbDTO(currentLat, currentLong, 0, "ASC", pageNo, mSessionManager.getUserId(),searchText);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(productListDbDTO);
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
        fetchList(1,"");
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(1, "");
            }
        });
        mProductAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.classified_ad_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.clasified_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(getApplicationContext()," "+query,Toast.LENGTH_LONG).show();
                searchText = query;
                sendSearchData(searchText);
                fetchList(1, searchText);
                mProductAdapter.clear();
                searchText="";
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
    public void sendSearchData(String str) {
        fetchList(1,searchText);
        //onRefresh();

    }
}
