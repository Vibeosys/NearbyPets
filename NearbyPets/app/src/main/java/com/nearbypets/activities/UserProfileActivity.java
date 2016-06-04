package com.nearbypets.activities;

import android.os.Bundle;

import com.nearbypets.R;
import com.nearbypets.views.RobotoMediumTextView;

public class UserProfileActivity extends BaseActivity {

    private RobotoMediumTextView mTxtName, mTxtEmail, mTxtMobNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle(getResources().getString(R.string.activity_profile));

        mTxtName = (RobotoMediumTextView) findViewById(R.id.txtName);
        mTxtEmail = (RobotoMediumTextView) findViewById(R.id.txtEmail);
        mTxtMobNo = (RobotoMediumTextView) findViewById(R.id.txtMobNo);

        mTxtName.setText(mSessionManager.getUserName());
        mTxtEmail.setText(mSessionManager.getUserEmailId());
        mTxtMobNo.setText(mSessionManager.getUserPhone());
    }
}
