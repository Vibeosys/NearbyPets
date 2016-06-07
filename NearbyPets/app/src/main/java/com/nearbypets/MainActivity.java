package com.nearbypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.activities.BaseActivity;
import com.nearbypets.activities.CategoryListActivity;
import com.nearbypets.activities.HiddenAdActivity;
import com.nearbypets.activities.LoginActivity;
import com.nearbypets.activities.PostMyAdActivity;
import com.nearbypets.activities.PostedAdDetailsActivity;
import com.nearbypets.activities.PostedAdListActivity;
import com.nearbypets.activities.ProductDescActivity;
import com.nearbypets.activities.ProductListActivity;
import com.nearbypets.activities.SavedAdListActivity;
import com.nearbypets.activities.SettingActivity;
import com.nearbypets.activities.UserProfileActivity;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.adapters.DashboardProductListAdapter;
import com.nearbypets.adapters.SortAdapter;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.ProductListDbDTO;
import com.nearbypets.data.SortDTO;
import com.nearbypets.data.downloaddto.DownloadProductDbDataDTO;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EndlessScrollListener;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.UserAuth;

import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener, ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived, DashboardProductListAdapter.CustomButtonListener, DashboardProductListAdapter.CustomItemListener {
    private ListView mListViewProduct;
    private DashboardProductListAdapter mProductAdapter;
    private CategoryAdapter mCategoryAdapter;
    private SortAdapter mSortAdapter;
    private Spinner spnSortBy;
    private SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawer;
    private final int REQ_TOKEN_LIST = 1;
    GPSTracker gpsTracker;
    private static int mSortOption = 0;
    private static String sort = "DESC";
    private static int storedPageNO = 0;
    private int adDisplay = 0;
    private Date dateToCompaire = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!UserAuth.isUserLoggedIn()) {
            // finish();
            callLogin();
        }
        storedPageNO = 0;
        gpsTracker = new GPSTracker(getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mListViewProduct = (ListView) findViewById(R.id.listCateogry);
        spnSortBy = (Spinner) findViewById(R.id.spnSortByMain);
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {

            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");


        }
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        dateToCompaire = null;
        adDisplay = 0;
        mSortAdapter = new SortAdapter(getApplicationContext());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSortAdapter.addItem(new SortDTO("Sort By", 0, "DESC"));
        mSortAdapter.addItem(new SortDTO("Date Desc", 0, "DESC"));
        mSortAdapter.addItem(new SortDTO("Date Asc", 0, "ASC"));
        mSortAdapter.addItem(new SortDTO("Price Desc", 2, "DESC"));
        mSortAdapter.addItem(new SortDTO("Price Asc", 2, "ASC"));
        mSortAdapter.addItem(new SortDTO("Distance Desc", 1, "DESC"));
        mSortAdapter.addItem(new SortDTO("Distance Asc", 1, "ASC"));
        spnSortBy.setAdapter(mSortAdapter);
        mProductAdapter = new DashboardProductListAdapter(this);
        mListViewProduct.setAdapter(mProductAdapter);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mProductAdapter.setCustomItemListner(this);
        mProductAdapter.setCustomButtonListner(this);
        
       /* View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView txtUserName = (TextView) headerView.findViewById(R.id.txtUserName);
        txtUserName.setText(mSessionManager.getUserName());
        TextView txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);
        txtEmail.setText(mSessionManager.getUserEmailId());*/

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchList(1, mSortOption, sort);
                                        //logic to refersh list
                                    }
                                }
        );
        switch (mSessionManager.getUserRollId()) {
            case AppConstants.ROLL_ID_ADMIN:
                navigationView.getMenu().clear(); //clear old inflated items.
                navigationView.inflateMenu(R.menu.activity_main_admin_drawer);
                break;
            case AppConstants.ROLL_ID_USER:
                navigationView.getMenu().clear(); //clear old inflated items.
                navigationView.inflateMenu(R.menu.activity_main_user_drawer);
                break;
        }
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        spnSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortDTO sortDTO = (SortDTO) mSortAdapter.getItem(position);
                dateToCompaire = null;
                mSortOption = sortDTO.getValue();
                sort = sortDTO.getSorting();
                swipeRefreshLayout.setRefreshing(true);
                mProductAdapter.clear();
                fetchList(1, mSortOption, sort);
                mListViewProduct.setOnScrollListener(new EndlessScrollListener
                        (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        customLoadMoreDataFromApi(page);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void customLoadMoreDataFromApi(int page) {
        fetchList(page, mSortOption, sort);
    }


    private void fetchList(int pageNo, int sortOption, String sort) {
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
            swipeRefreshLayout.setRefreshing(false);
        } else if (storedPageNO != pageNo) {
            ProductListDbDTO productListDbDTO = new ProductListDbDTO(gpsTracker.getLatitude(), gpsTracker.getLongitude(), sortOption, sort, pageNo);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(productListDbDTO);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PRODUCT_LIST, serializedJsonString);
            mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);
            storedPageNO = pageNo;
        }

    }

    //update the product list adapter
    private void updateList(ArrayList<ProductDbDTO> data) {
        //mProductAdapter.clear();
        ProDbDtoTOProDTO converter = new ProDbDtoTOProDTO(data);
        int id = Integer.parseInt(settingMap.get("FacebookAdPageSize"));
        ArrayList<ProductDataDTO> productDataDTOs = converter.getProductDTOs();
        // mProductAdapter.addSectionHeaderItem(productDataDTOs.get(0));
        if (mSortOption == 0) {
            for (int i = 0; i < productDataDTOs.size(); i++) {
                if (dateToCompaire != null) {
                    int compiare = dateToCompaire.compareTo(productDataDTOs.get(i).getPostedDt());
                    if (compiare != 0) {
                        mProductAdapter.addSectionHeaderItem(productDataDTOs.get(i));
                        dateToCompaire = productDataDTOs.get(i).getPostedDt();
                    } else {
                        mProductAdapter.addItem(productDataDTOs.get(i));
                        adDisplay = adDisplay + 1;
                    }
                } else {
                    mProductAdapter.addSectionHeaderItem(productDataDTOs.get(i));
                    dateToCompaire = productDataDTOs.get(i).getPostedDt();
                }
                if (adDisplay % id == 0) {
                    mProductAdapter.addSectionAdItem(productDataDTOs.get(i));
                }
                //mProductAdapter.addItem(productDataDTOs.get(i));
            }
        } else {
            for (int i = 0; i < productDataDTOs.size(); i++) {
                adDisplay = adDisplay + 1;
                mProductAdapter.addItem(productDataDTOs.get(i));
                if (adDisplay % id == 0)
                    mProductAdapter.addSectionAdItem(productDataDTOs.get(i));
            }
        }
        //
        //mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_detail) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        } else if (id == R.id.nav_my_posted_ads) {
            startActivity(new Intent(getApplicationContext(), PostedAdListActivity.class));
           /* Uri gmmIntentUri = Uri.parse("geo:0,0?q=1600 Amphitheatre Parkway, Mountain+View, California");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);*/

        } else if (id == R.id.nav_my_saved_ads) {
            startActivity(new Intent(getApplicationContext(), SavedAdListActivity.class));


        } else if (id == R.id.nav_view_categories) {
            startActivity(new Intent(getApplicationContext(), CategoryListActivity.class));

        } else if (id == R.id.nav_post_new_ad) {
            startActivity(new Intent(getApplicationContext(), PostMyAdActivity.class));

        } else if (id == R.id.nav_log_out) {
            try {
                LoginManager.getInstance().logOut();
            } catch (Exception e) {

            }
            UserAuth.CleanAuthenticationInfo();
            callLogin();

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));

        } else if (id == R.id.nav_hidden_ad) {
            startActivity(new Intent(getApplicationContext(), HiddenAdActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
//logic to refersh list
        mProductAdapter.clear();
        storedPageNO = 0;
        dateToCompaire = null;
        adDisplay = 0;
        fetchList(1, mSortOption, sort);
        mListViewProduct.setOnScrollListener(new EndlessScrollListener
                (Integer.parseInt(settingMap.get("ClassifiedAdPageSize"))) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
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

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "Error " + error.toString());
                swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void callLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData) {
        productData.setFavouriteFlag(!value);
        mProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(int position, ProductDataDTO productData) {
        ProductDataDTO productDataDTO = mProductAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), PostedAdDetailsActivity.class);
        intent.putExtra(AppConstants.PRODUCT_DISTANCE, productDataDTO.getDistance());
        intent.putExtra(AppConstants.PRODUCT_AD_ID, productDataDTO.getAdId());
        startActivity(intent);
    }
}
