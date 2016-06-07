package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDBDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadRegisterDbDTO;
import com.nearbypets.data.downloaddto.NotificationDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived, View.OnClickListener {

    EditText txtPageSize, txtFbAd, txtRadius;
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
        txtRadius = (EditText) findViewById(R.id.txtRadius);
        btnSave = (Button) findViewById(R.id.btnSave);
        formView = findViewById(R.id.formSettings);
        progressBar = findViewById(R.id.progressBar);
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");
        } else {

            btnSave.setOnClickListener(this);
            mServerSyncManager.setOnStringErrorReceived(this);
            mServerSyncManager.setOnStringResultReceived(this);
        }

    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_SETTINGS:
                showProgress(false, formView, progressBar);
                Log.d("RESULT", "##REQ" + error.toString());
                break;
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_SETTINGS:
                showProgress(false, formView, progressBar);
                Log.d("RESULT", "##REQ" + data.toString());
                try {
                    DownloadRegisterDbDTO download = new Gson().fromJson(data.toString(), DownloadRegisterDbDTO.class);
                    updateSettings(download.getSettings());
                    Log.i(TAG, download.toString());
                    checkLogin(download.getData().get(0).getData());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                break;
        }
    }

    private void checkLogin(ArrayList<NotificationDTO> data) {
        NotificationDTO notificationDTO = data.get(0);
        if (notificationDTO.getErrorCode() == 0) {
            Toast.makeText(getApplicationContext(), "" + notificationDTO.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            createAlertDialog("Save error", "" + notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
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
        settingsDBDTOs.add(new SettingsDBDTO("AdSearchDistanceInKM", txtRadius.getText().toString()));
        settingsDBDTOs.add(new SettingsDBDTO("ClassifiedAdPageSize", txtPageSize.getText().toString()));
        settingsDBDTOs.add(new SettingsDBDTO("FacebookAdPageSize", txtFbAd.getText().toString()));
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(settingsDBDTOs);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.SETTINGS, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_SETTINGS, tableDataDTO);
    }
}
