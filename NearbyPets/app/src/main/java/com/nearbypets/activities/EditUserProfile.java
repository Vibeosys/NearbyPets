package com.nearbypets.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.UpdateRegisterDBDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.UserDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.UserAuth;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserProfile extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived {

    private EditText mFirstName, mLastName, mEmailId, mPhoneNumber, mPasswordEdit;
    String mName, mEmail, mPhone, mLastN, mPassword;
    private Button mBtnSave, mBtnCancel;
    private View formView;
    private View progressBar;
    String firstNameStr;
    String lastNameStr;
    String emailStr;
    String password, phoneNumber;
    private static final int REQ_TOKEN_EDIT_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        setTitle("Edit User Profile");
        mFirstName = (EditText) findViewById(R.id.firstNameTv);
        mEmailId = (EditText) findViewById(R.id.EmailIdTv);
        mPhoneNumber = (EditText) findViewById(R.id.PhoneTv);
        mLastName = (EditText) findViewById(R.id.lastNameTv);
        mPasswordEdit = (EditText) findViewById(R.id.PassWdTv);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        formView = findViewById(R.id.profileLinear);
        progressBar = findViewById(R.id.progressBar);
        mPassword = mSessionManager.getUserPassword();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        try {
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    mName = extras.getString("FirstName");
                    mLastN = extras.getString("LastName");
                    mEmail = extras.getString("EmailId");
                    mPhone = extras.getString("Phone");
                    mFirstName.setText("" + mName);
                    mEmailId.setText("" + mEmail);
                    mPhoneNumber.setText("" + mPhone);
                    mLastName.setText("" + mLastN);
                    if (TextUtils.isEmpty(mPassword)) {
                        mPasswordEdit.setText("");
                    } else if (!TextUtils.isEmpty(mPassword)) {
                        mPasswordEdit.setText("" + mPassword);
                    }

                }
            } else {

            }
        } catch (Exception e) {
            Log.e(TAG, "Error to get data");

        }

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btnSave:
                        boolean cancelFlag = false;
                        View focusView = null;
                        firstNameStr = mFirstName.getText().toString().trim();
                        lastNameStr = mLastName.getText().toString().trim();
                        emailStr = mEmailId.getText().toString().trim();
                        password = mPasswordEdit.getText().toString().trim();
                        phoneNumber = mPhoneNumber.getText().toString().trim();
                        if (TextUtils.isEmpty(firstNameStr)) {
                            focusView = mFirstName;
                            mFirstName.setError("Please provide first name");
                            cancelFlag = true;

                        } else if (firstNameStr.length() < 2) {
                            focusView = mFirstName;
                            mFirstName.setError("First Name should have atleast 2 character");
                            cancelFlag = true;
                        } else if (firstNameStr.length() > 30) {
                            focusView = mFirstName;
                            mFirstName.setError("Maximum 30 characters are allowed");
                            cancelFlag = true;

                        } else if (TextUtils.isEmpty(lastNameStr))

                        {
                            focusView = mLastName;
                            mLastName.setError("Please provide Last name");
                            cancelFlag = true;
                        } else if (lastNameStr.length() < 2) {
                            focusView = mLastName;
                            mLastName.setError("Last Name should have atleast 2 character");
                            cancelFlag = true;
                        } else if (lastNameStr.length() > 30) {
                            focusView = mLastName;
                            mLastName.setError("Maximum 30 characters are allowed");
                            cancelFlag = true;

                        }
                        /*if (isValidEmail(emailStr)) {
                            focusView = mEmailId;
                            mEmailId.setError("Please Provide proper email id");
                            cancelFlag = true;
                        }else*/
                        if (TextUtils.isEmpty(emailStr)) {
                            focusView = mEmailId;
                            mEmailId.setError("Email Id cannot be blank");
                            cancelFlag = true;
                        } else if (phoneNumber.length() > 10) {
                            focusView = mPhoneNumber;
                            mPhoneNumber.setError("Phone number cannot be greater than 10 digit");
                            cancelFlag = true;
                        }
                        if (cancelFlag) {
                            focusView.setFocusable(true);
                        } else {

                            CallToUpdateRegister();
                        }
                }
            }


        });

    }

    private void CallToUpdateRegister() {

        showProgress(true, formView, progressBar);
        UpdateRegisterDBDTO updateRegisterDBDTO = new UpdateRegisterDBDTO(mSessionManager.getUserId()
                , mFirstName.getText().toString().trim(), mLastName.getText().toString().trim(), mPhoneNumber.getText().toString().trim()
                , mEmailId.getText().toString().trim(), mPasswordEdit.getText().toString().trim());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(updateRegisterDBDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.EDIT_USER_PROFILE, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_EDIT_PROFILE, tableDataDTO);
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
       /* switch (requestToken) {
            case REQ_TOKEN_EDIT_PROFILE:
                showProgress(false, formView, progressBar);
                Toast.makeText(getApplicationContext(),"Profile updated Successfully",Toast.LENGTH_LONG).show();
                Intent userProfile = new Intent(getApplicationContext(),UserProfileActivity.class);
                startActivity(userProfile);
                finish();
        }*/

    }


    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {

        switch (requestToken) {
            case REQ_TOKEN_EDIT_PROFILE:
                showProgress(false, formView, progressBar);
                Toast.makeText(getApplicationContext(), "Profile updated Successfully", Toast.LENGTH_LONG).show();
                UserDbDTO userDbDTO = UserDbDTO.deserializeJson(data);
                UserAuth userAuth = new UserAuth();
                userAuth.saveAuthenticationInfo(userDbDTO, getApplicationContext());
                Intent userProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(userProfile);
                finish();
        }

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {

    }
}
