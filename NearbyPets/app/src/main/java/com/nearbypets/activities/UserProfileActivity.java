package com.nearbypets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView mTxtName,mTxtLastName ,mTxtEmail, mTxtMobNo;
    private Button editProfile;
    UserDbDTO userDbDTO;

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
        mTxtName = (TextView) findViewById(R.id.FirstNameTitle);
        mTxtLastName = (TextView)findViewById(R.id.LastNameTitle);
        mTxtEmail = (TextView) findViewById(R.id.emailIdDeitText);
        mTxtMobNo = (TextView) findViewById(R.id.phoneNumber);
        editProfile = (Button)findViewById(R.id.editProfile);
        formView = findViewById(R.id.profileLinear);
        progressBar = findViewById(R.id.progressBar);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Edit profile is clicked",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),EditUserProfile.class);
                intent.putExtra("FirstName",userDbDTO.getFname().toString());
                intent.putExtra("LastName",userDbDTO.getLname().toString());
                intent.putExtra("EmailId",mTxtEmail.getText().toString());
                intent.putExtra("Phone",mTxtMobNo.getText().toString());
                startActivity(intent);
                finish();
            }
        });
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
                userDbDTO = UserDbDTO.deserializeJson(data);
                mTxtName.setText(userDbDTO.getFname());
                mTxtLastName.setText(userDbDTO.getLname());
                mTxtEmail.setText(userDbDTO.getEmail());
                mTxtMobNo.setText(userDbDTO.getPhone());
        }

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {

        switch (requestToken) {
            case REQ_TOKEN_PROFILE:

        }

    }

}

