package com.nearbypets.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nearbypets.R;
import com.nearbypets.adapters.ProductListAdapter;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.utils.AppConstants;

public class ClassifiedAdsActivity extends ProductListActivity implements
        ProductListAdapter.CustomButtonListener, ProductListAdapter.CustomItemListener {

    private int mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_list);
        setTitle("Classified Ads");
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        mCategoryId = getIntent().getExtras().getInt(AppConstants.CATEGORY_ID);
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
        ProductDataDTO productDataDTO = mProductAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), ClassfiedProductDescActivity.class);
        intent.putExtra(AppConstants.PRODUCT_DISTANCE, "" + productDataDTO.getDistance());
        intent.putExtra(AppConstants.PRODUCT_AD_ID, "48E3B44B-801A-B129-B5A4-BE8387956F63");
        startActivity(intent);
    }
}
