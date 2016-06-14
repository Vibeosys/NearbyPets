package com.nearbypets.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nearbypets.R;
import com.nearbypets.adapters.ProductListAdapter;
import com.nearbypets.adapters.SortAdapter;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.SortDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.SaveAnAdDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.ServerSyncManager;

import java.util.List;

public class ProductListActivity extends BaseActivity implements
        ProductListAdapter.CustomButtonListener, ProductListAdapter.CustomItemListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener,
        ProductListAdapter.CustomHideListener, ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived {

    protected ListView mListViewProduct;
    protected ProductListAdapter mProductAdapter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected SortAdapter mSortAdapter;
    protected Spinner spnSortBy;
    protected static int mSortOption = 0;
    protected static String sort = "DESC";
    protected View formView;
    protected View progressBar;

    protected final int REQ_TOKEN_LIST = 1;
    protected final int REQ_TOKEN_HIDE_AD = 2;
    protected final int REQ_TOKEN_SAVE_AD = 3;
    protected final int REQ_TOKEN_GET_HIDDEN_LIST = 33;
    protected final int REQ_TOKEN_POST_HIDDEN_AD = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(getResources().getString(R.string.activity_product_list));
        mListViewProduct = (ListView) findViewById(R.id.productList);
        spnSortBy = (Spinner) findViewById(R.id.spnSortBy);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSortAdapter = new SortAdapter(getApplicationContext());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSortAdapter.addItem(new SortDTO("Sort By", 0, "DESC"));
        mSortAdapter.addItem(new SortDTO("Date Desc", 0, "DESC"));
        mSortAdapter.addItem(new SortDTO("Date Asc", 0, "ASC"));
        mSortAdapter.addItem(new SortDTO("Price Desc", 2, "DESC"));
        mSortAdapter.addItem(new SortDTO("Price Asc", 2, "ASC"));
        mSortAdapter.addItem(new SortDTO("Distance Desc", 1, "DESC"));
        mSortAdapter.addItem(new SortDTO("Distance Asc", 1, "ASC"));
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnSortBy.setAdapter(mSortAdapter);
        spnSortBy.setOnItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        mProductAdapter = new ProductListAdapter(this, mSessionManager.getUserRoleId());
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        mProductAdapter.setCustomHideListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mListViewProduct.setAdapter(mProductAdapter);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                }
        );
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //customLoadMoreDataFromApi(page);
                return true;
            }
        });
    }

    protected void callToSaveAd(String adId) {
        showProgress(true, formView, progressBar);
        SaveAnAdDbDTO saveAnAdDbDTO = new SaveAnAdDbDTO(adId, mSessionManager.getUserId());
        String serializedJsonString = saveAnAdDbDTO.serializeString();
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SAVE_AN_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_SAVE_AD, tableDataDTO);
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
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onHideClickListener(int position, ProductDataDTO productData) {

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        showProgress(false, formView, progressBar);

    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        showProgress(false, formView, progressBar);
        if (requestToken == REQ_TOKEN_SAVE_AD && errorDbDTO.getErrorCode() != 0) {
            createAlertDialog("Error", errorDbDTO.getMessage());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        showProgress(false, formView, progressBar);
        if (requestToken == REQ_TOKEN_SAVE_AD) {
            Toast.makeText(getApplicationContext(), "Ad saved sucessfully", Toast.LENGTH_SHORT).show();
        }
    }
}
