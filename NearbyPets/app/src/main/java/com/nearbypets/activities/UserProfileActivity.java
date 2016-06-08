package com.nearbypets.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.ProfileDbDTO;
import com.nearbypets.data.downloaddto.UserDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.views.RobotoMediumTextView;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived {

    private static final int REQ_TOKEN_PROFILE = 1;
    private RobotoMediumTextView mTxtName, mTxtEmail, mTxtMobNo;
    private View formView, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle(getResources().getString(R.string.activity_profile));

        mTxtName = (RobotoMediumTextView) findViewById(R.id.txtName);
        mTxtEmail = (RobotoMediumTextView) findViewById(R.id.txtEmail);
        mTxtMobNo = (RobotoMediumTextView) findViewById(R.id.txtMobNo);
        formView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        mTxtName.setText("");
        mTxtEmail.setText("");
        mTxtMobNo.setText("");
        callToProfile();
    }

    private void callToProfile() {
        showProgress(true, formView, progressBar);
        ProfileDbDTO profileDbDTO = new ProfileDbDTO(mSessionManager.getUserEmailId());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(profileDbDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.GET_PROFILE, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_PROFILE, tableDataDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        showProgress(true, formView, progressBar);
        createAlertDialog("Server error!!!", "Try Again Later");
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        createAlertDialog("Login error", "" + errorDbDTO.getMessage());
        Log.i("TAG", "##" + errorDbDTO.getMessage());
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        UserDbDTO userDbDTO = UserDbDTO.deserializeJson(data);
        mTxtName.setText(userDbDTO.getFname() + " " + userDbDTO.getLname());
        mTxtEmail.setText(userDbDTO.getEmail());
        mTxtMobNo.setText(userDbDTO.getPhone());
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {

    }
}

