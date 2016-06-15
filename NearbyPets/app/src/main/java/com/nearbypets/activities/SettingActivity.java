package com.nearbypets.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDBDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {

    EditText txtPageSize, txtFbAd;
    Button btnSave;
    private final int REQ_TOKEN_SETTINGS = 1;
    private View formView;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(getResources().getString(R.string.activity_setting));
        txtPageSize = (EditText) findViewById(R.id.txtPageSize);
        txtFbAd = (EditText) findViewById(R.id.txtFbAd);
        btnSave = (Button) findViewById(R.id.btnSave);
        formView = findViewById(R.id.formSettings);
        progressBar = findViewById(R.id.progressBar);
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check network connection");
        } else {

            btnSave.setOnClickListener(this);
            mServerSyncManager.setOnStringErrorReceived(this);
            mServerSyncManager.setOnStringResultReceived(this);
        }

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        showProgress(false, formView, progressBar);
        Log.d("RESULT", "##REQ" + error.toString());
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        showProgress(false, formView, progressBar);
        if (errorDbDTO.getErrorCode() != 0) {
            createAlertDialog("Save error", "" + errorDbDTO.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSave:
                saveSettings();
                break;
        }
    }

    private void saveSettings() {
        showProgress(true, formView, progressBar);
        ArrayList<SettingsDBDTO> settingsDBDTOs = new ArrayList<>();
        settingsDBDTOs.add(new SettingsDBDTO("ClassifiedAdPageSize", txtPageSize.getText().toString()));
        settingsDBDTOs.add(new SettingsDBDTO("FacebookAdPageSize", txtFbAd.getText().toString()));
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(settingsDBDTOs);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SETTINGS, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_SETTINGS, tableDataDTO);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        showProgress(false, formView, progressBar);
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        showProgress(false, formView, progressBar);
//        Log.d("RESULT", "##REQ" + data.toString());
        updateSettings(settings);
        Toast.makeText(getApplicationContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show();
        //checkLogin(download.getData().get(0).getData());
    }
}
