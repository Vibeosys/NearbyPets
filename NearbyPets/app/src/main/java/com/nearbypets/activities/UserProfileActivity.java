package com.nearbypets.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
   // private RobotoMediumTextView  mTxtName,  mTxtEmail, mTxtMobNo;
    private View formView, progressBar;
    TextView mTxtName,mTxtEmail,mTxtMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_user_profile);
        setContentView(R.layout.new_profile);
        setTitle(getResources().getString(R.string.activity_profile));
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        /*mTxtName = (RobotoMediumTextView) findViewById(R.id.txtName);
        mTxtEmail = (RobotoMediumTextView) findViewById(R.id.txtEmail);
        mTxtMobNo = (RobotoMediumTextView) findViewById(R.id.txtMobNo);*/
        mTxtName =(TextView) findViewById(R.id.FirstNameTitle);
        mTxtEmail=(TextView) findViewById(R.id.emailIdTitle);
        mTxtMobNo=(TextView) findViewById(R.id.phoneNumber);
        formView = findViewById(R.id.profileLinear);
        progressBar = findViewById(R.id.progressBar);

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
       showProgress(false, formView, progressBar);
        createAlertDialog("Server error!!!", "Try Again Later");
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        showProgress(false, formView, progressBar);
        createAlertDialog("Login error", "" + errorDbDTO.getMessage());
        Log.i("TAG", "##" + errorDbDTO.getMessage());
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_PROFILE:
                showProgress(false, formView, progressBar);
                UserDbDTO userDbDTO = UserDbDTO.deserializeJson(data);

                mTxtName.setText(userDbDTO.getFname().toString() + " " + userDbDTO.getLname().toString());
                mTxtEmail.setText(userDbDTO.getEmail());
                mTxtMobNo.setText(userDbDTO.getPwd());
        }

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {

        switch (requestToken) {
            case REQ_TOKEN_PROFILE:

        }

    }

}

