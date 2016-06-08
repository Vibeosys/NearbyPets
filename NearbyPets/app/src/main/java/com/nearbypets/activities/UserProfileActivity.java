package com.nearbypets.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.volley.VolleyError;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.views.RobotoMediumTextView;

import java.util.ArrayList;

public class UserProfileActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived {

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
        formView=findViewById(R.id.)
        mTxtName.setText("");
        mTxtEmail.setText("");
        mTxtMobNo.setText("");
        callToProfile();
    }

    private void callToProfile() {
        /*showProgress(true, formView, progressBar);
        LoginDBDTO login = new LoginDBDTO(mEmailId.getText().toString(), mPassword.getText().toString());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(login);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_LOGIN, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_LOGIN, tableDataDTO);*/
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull ArrayList<SettingsDTO> settings, int requestToken) {

    }
}
