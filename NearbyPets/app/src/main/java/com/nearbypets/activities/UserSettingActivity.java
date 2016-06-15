package com.nearbypets.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.nearbypets.data.UserSettingDbDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.ArrayList;
import java.util.List;

public class UserSettingActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    EditText txtRadius;
    Button btnSave;
    private final int REQ_TOKEN_SETTINGS = 1;
    private View formView;
    private View progressBar;
    int radius = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        setTitle(getResources().getString(R.string.activity_setting));
        txtRadius = (EditText) findViewById(R.id.txtRadius);
        txtRadius.setText("" + mSessionManager.getRadiusInKm());
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

        try {
            radius = Integer.parseInt(txtRadius.getText().toString());
        } catch (Exception e) {
            radius = 5000;
        }
        UserSettingDbDTO userSettingDbDTO = new UserSettingDbDTO(mSessionManager.getUserId(), radius);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(userSettingDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_SETTINGS, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_SETTINGS, tableDataDTO);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        showProgress(false, formView, progressBar);
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        showProgress(false, formView, progressBar);
        // Log.d("RESULT", "##REQ" + data.toString());
        mSessionManager.setRadiusInKm(radius);
        updateSettings(settings);
        Toast.makeText(getApplicationContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show();
        //checkLogin(download.getData().get(0).getData());
    }
}
