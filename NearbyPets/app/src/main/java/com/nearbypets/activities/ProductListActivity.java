package com.nearbypets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.nearbypets.R;
import com.nearbypets.adapters.ProductListAdapter;
import com.nearbypets.data.BaseDTO;
import com.nearbypets.data.ProductDataDTO;

import java.util.ArrayList;

public class ProductListActivity extends BaseActivity implements ProductListAdapter.CustomButtonListener, ProductListAdapter.CustomItemListener {

    protected ListView mListViewProduct;
    protected ProductListAdapter mProductAdapter;

    protected ArrayAdapter<String> mSortAdapter;
    protected Spinner spnSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(getResources().getString(R.string.activity_product_list));
        mListViewProduct = (ListView) findViewById(R.id.productList);
        spnSortBy = (Spinner) findViewById(R.id.spnSortBy);

        String[] category = {"Sort By", "Date Desc", "Date Asc", "Price Desc", "Price Asc", "Distance Desc", "Distance Asc"};
        mSortAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, category);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnSortBy.setAdapter(mSortAdapter);


        mProductAdapter = new ProductListAdapter(this, mSessionManager.getUserRollId());
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        mProductAdapter.addItem(new ProductDataDTO("Product Title1", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "14/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title2", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "14/05/2016"));

        mProductAdapter.addItem(new ProductDataDTO("Product Title3", "boxdogs", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, true, "13/05/2016"));

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "14/05/2016"));

        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "13/05/2016"));

        mProductAdapter.addItem(new ProductDataDTO("Product Title5", "boxdogs", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, true, "12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title6", "boxbirds", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, true, "12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title7", "boxcats", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "12/05/2016"));
        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "fbtestad", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", 10, 100, false, "14/05/2016"));

        mListViewProduct.setAdapter(mProductAdapter);
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
}
