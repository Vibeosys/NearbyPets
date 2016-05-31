package com.nearbypets;

import android.content.Intent;
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
import android.widget.ListView;

import com.nearbypets.activities.CategoryListActivity;
import com.nearbypets.activities.LoginActivity;
import com.nearbypets.activities.PostMyAdActivity;
import com.nearbypets.activities.ProductDescActivity;
import com.nearbypets.activities.ProductListActivity;
import com.nearbypets.activities.SettingActivity;
import com.nearbypets.activities.UserProfileActivity;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.data.CategoryDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView mCategoryList;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCategoryList = (ListView) findViewById(R.id.listCateogry);

        ArrayList<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO("Birds", 23, "birds", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Food", 18, "food", getResources().getDrawable(R.drawable.food)));
        categories.add(new CategoryDTO("Reptiles", 9, "reptiles", getResources().getDrawable(R.drawable.reptiles)));
        categories.add(new CategoryDTO("Cats", 276, "cats", getResources().getDrawable(R.drawable.cats)));
        categories.add(new CategoryDTO("Birds", 276, "birds", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Food", 276, "food", getResources().getDrawable(R.drawable.food)));
        mCategoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
        mCategoryList.setAdapter(mCategoryAdapter);
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

        mCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
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

        } else if (id == R.id.nav_my_saved_ads) {
            startActivity(new Intent(getApplicationContext(), ProductListActivity.class));


        }/* else if (id == R.id.nav_view_categories) {
            startActivity(new Intent(getApplicationContext(), CategoryListActivity.class));

        }*/ else if (id == R.id.nav_post_new_ad) {
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
