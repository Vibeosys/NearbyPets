package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.nearbypets.R;
import com.nearbypets.adapters.CategoryAdapter;
import com.nearbypets.data.CategoryDTO;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    private ListView mCategoryList;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mCategoryList = (ListView) findViewById(R.id.listCateogry);

        ArrayList<CategoryDTO> categories = new ArrayList<>();
        categories.add(new CategoryDTO("Category", 23, "Category.jpg", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Category", 18, "Category.jpg", getResources().getDrawable(R.drawable.food)));
        categories.add(new CategoryDTO("Category", 9, "Category.jpg", getResources().getDrawable(R.drawable.reptiles)));
        categories.add(new CategoryDTO("Category", 276, "Category.jpg", getResources().getDrawable(R.drawable.cats)));
        categories.add(new CategoryDTO("Category", 276, "Category.jpg", getResources().getDrawable(R.drawable.birds)));
        categories.add(new CategoryDTO("Category", 276, "Category.jpg", getResources().getDrawable(R.drawable.food)));
        mCategoryAdapter = new CategoryAdapter(getApplicationContext(), categories);
        mCategoryList.setAdapter(mCategoryAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }
}
