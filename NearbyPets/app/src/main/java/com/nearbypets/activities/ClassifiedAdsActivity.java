package com.nearbypets.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.ClassifiedDbDTO;
import com.nearbypets.data.HiddenAdDbDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.SortDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedAdsActivity extends ProductListActivity implements
        ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {
    private static int storedPageNO = 0;
    private int mCategoryId;
    //GPSTracker gpsTracker;

    private int adDisplay = 0;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_list);
        setTitle("Classified Ads");
        getCurrentLocation(mLocationManager);
        mCategoryId = getIntent().getIntExtra(AppConstants.CATEGORY_ID, 0);
        //gpsTracker = new GPSTracker(getApplicationContext());
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
                                        fetchList(1, mSortOption, sort, searchText);
                                    }
                                }
        );
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort, searchText);
            }
        });

    }

    private void fetchList(int pageNo, int sortOption, String sort, String searchText) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            storedPageNO = pageNo;
            ClassifiedDbDTO productListDbDTO = new ClassifiedDbDTO(currentLat, currentLong, sortOption, sort, pageNo, mCategoryId, searchText);
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
            case REQ_TOKEN_HIDE_AD:
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
            case REQ_TOKEN_HIDE_AD:
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
        //super.onResultReceived(data, settings, requestToken);
        updateSettings(settings);
        switch (requestToken) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "data" + data);
                ArrayList<ProductDbDTO> productDbDTOs = ProductDbDTO.deserializeToArray(data);
                updateList(productDbDTOs);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case REQ_TOKEN_HIDE_AD:
                Toast.makeText(getApplicationContext(), "Ad is now hidden from clients", Toast.LENGTH_SHORT).show();
                break;
            case REQ_TOKEN_POST_HIDDEN_AD:
                Toast.makeText(getApplicationContext(), "Ad is now hidden from clients", Toast.LENGTH_SHORT).show();
                Intent categoryList = new Intent(getApplicationContext(), CategoryListActivity.class);
                startActivity(categoryList);
                break;
            case REQ_TOKEN_SAVE_AD:
                Toast.makeText(getApplicationContext(), "Ad is added to your favorites", Toast.LENGTH_SHORT).show();
                break;
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
        fetchList(1, mSortOption, sort, searchText);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort, searchText);
            }
        });
    }

    @Override
    public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData) {
        productData.setFavouriteFlag(!value);
        //createAlertDialog("Not yet implemented", "N/A");
        String adId= productData.getAdId();
        callToSaveAd(adId);
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
        fetchList(1, mSortOption, sort, searchText);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchList(page, mSortOption, sort, searchText);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onHideClickListener(int position, ProductDataDTO productData) {

        HiddenAdDbDTO hiddenAdDbDTO = new HiddenAdDbDTO(productData.getAdId(), Integer.parseInt(AppConstants.HIDE_AD_ADMIN));
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(hiddenAdDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.HIDDIN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_POST_HIDDEN_AD, tableDataDTO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.classified_ad_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.clasified_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast.makeText(getApplicationContext()," "+query,Toast.LENGTH_LONG).show();
                searchText = query;
                sendSearchData(searchText);
                fetchList(1, mSortOption, sort, searchText);
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
                fetchList(1, mSortOption, sort, searchText);
                //onRefresh();
                // Toast.makeText(getApplicationContext(),"Close button is clicked",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return true;
    }

    public void sendSearchData(String str) {
        fetchList(1, mSortOption, sort, searchText);
        onRefresh();

    }
}
