package com.nearbypets.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nearbypets.R;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.data.CategoryDTO;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mCategoryList;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mCategoryList = (ListView) findViewById(R.id.listCateogry);
        setTitle(getResources().getString(R.string.activity_category));
        ArrayList<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO("Birds", 23, "birds", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Food", 18, "food", getResources().getDrawable(R.drawable.food)));
        categories.add(new CategoryDTO("Reptiles", 9, "reptiles", getResources().getDrawable(R.drawable.reptiles)));
        categories.add(new CategoryDTO("Cats", 276, "cats", getResources().getDrawable(R.drawable.cats)));
        categories.add(new CategoryDTO("Birds", 276, "birds", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Food", 276, "food", getResources().getDrawable(R.drawable.food)));
        mCategoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
        mCategoryList.setAdapter(mCategoryAdapter);
        mCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
            }
        });
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
}
