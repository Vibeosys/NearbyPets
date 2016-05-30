package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nearbypets.R;
import com.nearbypets.adapters.ProductListAdapter;
import com.nearbypets.data.ProductDataDTO;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ListView mListViewProduct;
    private ProductListAdapter mProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mListViewProduct = (ListView) findViewById(R.id.productList);
        mProductAdapter = new ProductListAdapter(this);

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title1", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title1", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title2", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 14/05/2016"));

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title3", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title3", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title4", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 13/05/2016"));

        mProductAdapter.addSectionHeaderItem(new ProductDataDTO("Product Title5", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title5", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title6", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));
        mProductAdapter.addItem(new ProductDataDTO("Product Title7", "image.jpg", "Lorem ipsum dolor sit amet," +
                "consectetur adipiscing elit.", "10 kilometers away from you", 100, false, "Posted On 12/05/2016"));

        mListViewProduct.setAdapter(mProductAdapter);

    }
}
