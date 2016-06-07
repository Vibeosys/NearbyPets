package com.nearbypets.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.converter.CateDbToCateDTO;
import com.nearbypets.data.CategoryDTO;
import com.nearbypets.data.CategoryDbDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.CategoryListDBDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryListActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    private ListView mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int REQ_TOKEN_CATEGORY_LIST = 5;
    private ArrayList<CategoryDTO> mCategoryDTOs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mCategoryList = (ListView) findViewById(R.id.listCateogry);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_category);
        setTitle(getResources().getString(R.string.activity_category));
        swipeRefreshLayout.setOnRefreshListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mCategoryAdapter = new CategoryAdapter(getApplicationContext());
        mCategoryList.setAdapter(mCategoryAdapter);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchCategoryList();
            }
        });


        mCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryDTO categoryDTO = (CategoryDTO) mCategoryAdapter.getItem(position);
                Intent iClassified = new Intent(getApplicationContext(), ClassifiedAdsActivity.class);
                iClassified.putExtra(AppConstants.CATEGORY_ID, categoryDTO.getCategoryId());
                startActivity(iClassified);
            }
        });
    }

    private void fetchCategoryList() {
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CATEGORY_LIST);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_CATEGORY_LIST, tableDataDTO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }
        if (id == R.id.new_ad) {
            startActivity(new Intent(getApplicationContext(), PostMyAdActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {
        fetchCategoryList();
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_CATEGORY_LIST:
                Log.d("Error", "##REQ" + error.toString());
                break;


        }

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {

        switch (requestTokan) {
            case REQ_TOKEN_CATEGORY_LIST:
                Log.d(TAG, "## Result response" + data.toString());
                try {
                    CategoryListDBDTO categoryListDBDTO = new Gson().fromJson(data.toString(), CategoryListDBDTO.class);
                    updateSettings(categoryListDBDTO.getSettings());
                    updateCategoryList(categoryListDBDTO.getData());
                    Log.i(TAG, categoryListDBDTO.toString());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                swipeRefreshLayout.setRefreshing(false);
                break;
        }

    }

    private void updateCategoryList(ArrayList<CategoryDbDTO> data) {
        mCategoryAdapter.clear();
        CateDbToCateDTO converter = new CateDbToCateDTO(data);
        mCategoryDTOs = converter.getCategoryDTOs();
        for (CategoryDTO category : mCategoryDTOs
                ) {
            mCategoryAdapter.addItem(category);
        }
        mCategoryAdapter.notifyDataSetChanged();

    }


}
