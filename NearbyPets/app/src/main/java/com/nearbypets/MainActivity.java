package com.nearbypets;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nearbypets.activities.CategoryListActivity;
import com.nearbypets.activities.LoginActivity;
import com.nearbypets.activities.PostMyAdActivity;
import com.nearbypets.activities.ProductDescActivity;
import com.nearbypets.activities.ProductListActivity;
import com.nearbypets.activities.SettingActivity;
import com.nearbypets.activities.UserProfileActivity;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.adapters.DashboardProductListAdapter;
import com.nearbypets.adapters.ProductListAdapter;
import com.nearbypets.data.CategoryDTO;
import com.nearbypets.data.ProductDataDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView mListViewProduct;
    private DashboardProductListAdapter mProductAdapter;
    private CategoryAdapter mCategoryAdapter;
    private ArrayAdapter<String> mSortAdapter;
    private Spinner spnSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListViewProduct = (ListView) findViewById(R.id.listCateogry);
        spnSortBy = (Spinner) findViewById(R.id.spnSortByMain);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://nearby-pets.appspot.com/test";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        String[] category = {"Sort By", "Date Desc", "Date Asc", "Price Desc", "Price Asc", "Distance Desc", "Distance Asc"};
        mSortAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, category);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnSortBy.setAdapter(mSortAdapter);
        mProductAdapter = new DashboardProductListAdapter(this);

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title1", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title2", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title3", "boxdogs", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, true, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));

        mProductAdapter.addSectionAdItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title5", "boxdogs", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, true, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title6", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, true, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title7", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));
        mProductAdapter.addSectionAdItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));

        mListViewProduct.setAdapter(mProductAdapter);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ProductDescActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
           /* Uri gmmIntentUri = Uri.parse("geo:0,0?q=1600 Amphitheatre Parkway, Mountain+View, California");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);*/

        } else if (id == R.id.nav_my_saved_ads) {
            startActivity(new Intent(getApplicationContext(), ProductListActivity.class));


        } else if (id == R.id.nav_view_categories) {
            startActivity(new Intent(getApplicationContext(), CategoryListActivity.class));

        } else if (id == R.id.nav_post_new_ad) {
            startActivity(new Intent(getApplicationContext(), PostMyAdActivity.class));

        } else if (id == R.id.nav_log_out) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), SettingActivity.class));

        } else if (id == R.id.nav_hidden_ad) {
            startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
