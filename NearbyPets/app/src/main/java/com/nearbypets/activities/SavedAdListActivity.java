package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.converter.ProDbDtoTOProDTO;
import com.nearbypets.data.GetSavedAd;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.data.ProductDbDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadProductDbDataDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class SavedAdListActivity extends ProductListActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    GPSTracker gpsTracker;
    private final int REQ_TOKEN_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_saved_ad_list);

        setTitle("Saved Ads");
        gpsTracker = new GPSTracker(getApplicationContext());
        spnSortBy.setVisibility(View.GONE);
        mProductAdapter.setActivityFlag(AppConstants.POSTED_AD_FLAG_ADAPTER);
        mProductAdapter.setCustomButtonListner(this);
        mProductAdapter.setCustomItemListner(this);
        fetchList();
    }

    private void fetchList() {
        mProductAdapter.clear();
        //Toast.makeText(getApplicationContext(), "lat " + gpsTracker.getLatitude() + "lng" + gpsTracker.getLongitude(), Toast.LENGTH_SHORT).show();
        GetSavedAd productListDbDTO = new GetSavedAd(gpsTracker.getLatitude(), gpsTracker.getLongitude(), mSessionManager.getUserId());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(productListDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SAVED_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_LIST, tableDataDTO);
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_LIST:
                Log.i("TAG", "Error " + error.toString());
                //swipeRefreshLayout.setRefreshing(false);
        }
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
                break;
        }

    }

    private void updateList(ArrayList<ProductDbDTO> data) {
        //mProductAdapter.clear();
        ProDbDtoTOProDTO converter = new ProDbDtoTOProDTO(data);
        ArrayList<ProductDataDTO> productDataDTOs = converter.getProductDTOs();
        for (int i = 0; i < productDataDTOs.size(); i++) {
            mProductAdapter.addItem(productDataDTOs.get(i));
        }
        //
        //mProductAdapter.notifyDataSetChanged();
    }
}
