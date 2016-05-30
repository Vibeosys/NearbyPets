package com.nearbypets.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nearbypets.R;

public class PostMyAdActivity extends AppCompatActivity {

    private ArrayAdapter<String> mCategoryAdapter;
    private ArrayAdapter<String> mTypeAdapter;
    private Spinner spnCategory;
    private Spinner spnType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_add);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnType = (Spinner) findViewById(R.id.spnType);

        String[] category = {"Parrot", "Cats", "Dogs"};
        mCategoryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, category);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnCategory.setAdapter(mCategoryAdapter);

        String[] type = {"For Sale", "For Adoption", "Lost/Found"};
        mTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, type);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnType.setAdapter(mTypeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }
}
