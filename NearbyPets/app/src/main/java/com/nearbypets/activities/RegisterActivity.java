package com.nearbypets.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.R;
import com.nearbypets.data.RegisterDBDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadRegisterDbDTO;
import com.nearbypets.data.downloaddto.NotificationDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived, View.OnClickListener {
    private EditText mRegisterEmailId, mRegisterFirstName, mRegisterLastName,
            mRegisterPassword, mRegisterPhoneNumber;
    private Button mRegister;
    private final int REQ_TOKEN_REGISTER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();
        setTitle(getResources().getString(R.string.activity_register));
        mRegisterFirstName = (EditText) findViewById(R.id.firstNameEditText);
        mRegisterLastName = (EditText) findViewById(R.id.lastNameEditText);
        mRegisterPhoneNumber = (EditText) findViewById(R.id.phoneNoEditText);
        mRegisterEmailId = (EditText) findViewById(R.id.emailIdEditText);
        mRegisterPassword = (EditText) findViewById(R.id.passwordEditText);
        mRegister = (Button) findViewById(R.id.register_user);

        mRegisterFirstName.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterLastName.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterPhoneNumber.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterPassword.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));

        mRegister.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register_user:
                boolean cancelFlag = false;
                View focusView = null;

                String emailStr = mRegisterEmailId.getText().toString().trim();
                String password = mRegisterPassword.getText().toString().trim();
                String firstNameStr = mRegisterFirstName.getText().toString().trim();
                String LastNameStr = mRegisterLastName.getText().toString().trim();
                mRegisterFirstName.setError(null);
                mRegisterLastName.setError(null);
                if (TextUtils.isEmpty(firstNameStr)) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("Please provide first name");
                    cancelFlag = true;

                } else if (firstNameStr.toString().trim().length() < 2) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("First Name should have atleast 2 character");
                    cancelFlag = true;
                } else if (firstNameStr.toString().trim().length() > 30) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("Maximum 30 characters are allowed");
                    cancelFlag = true;

                }
                if (TextUtils.isEmpty(LastNameStr))

                {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Please provide Last name");
                    cancelFlag = true;
                } else if (LastNameStr.toString().trim().length() < 2) {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Last Name should have atleast 2 character");
                    cancelFlag = true;
                } else if (LastNameStr.toString().trim().length() > 30) {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Maximum 30 characters are allowed");
                    cancelFlag = true;

                }
                if (!isValidEmail(emailStr) || TextUtils.isEmpty(emailStr)) {
                    focusView = mRegisterEmailId;
                    mRegisterEmailId.setError("Please Provide proper email id");
                    cancelFlag = true;
                }
                if (TextUtils.isEmpty(password)) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Password field cannot be blank");
                    cancelFlag = true;
                } else if (password.toString().trim().length() < 4) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Password should have minimum 4 character");
                    cancelFlag = true;
                } else if (password.toString().trim().length() > 20) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Maximium 20 characters are allowed");
                    cancelFlag = true;
                }
                if (cancelFlag) {
                    focusView.requestFocus();
                } else {
                    callToRegister();
                    Toast.makeText(getApplicationContext(), "All validation are done", Toast.LENGTH_LONG).show();
                }
        }

    }

    public void callToRegister() {
        RegisterDBDTO register = new RegisterDBDTO(mRegisterFirstName.getText().toString(),
                mRegisterLastName.getText().toString(), mRegisterEmailId.getText().toString(),
                mRegisterPassword.getText().toString(), mRegisterPhoneNumber.getText().toString());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(register);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_REGISTER, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_REGISTER, tableDataDTO);
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_REGISTER:
                Log.d("Error", "##REQ" + error.toString());
                break;
        }

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_REGISTER:
                Log.d("RESULT", "##REQ" + data.toString());
                try {
                    DownloadRegisterDbDTO download = new Gson().fromJson(data.toString(), DownloadRegisterDbDTO.class);
                    updateSettings(download.getSettings());
                    Log.i(TAG, download.toString());
                    checkRegistration(download.getData());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                break;
        }
    }

    private void checkRegistration(ArrayList<NotificationDTO> notificationDTOs) {

        NotificationDTO notificationDTO = notificationDTOs.get(0);
        if (notificationDTO.getErrorCode() == 0) {
            Log.i("TAG", "##" + notificationDTO.getMessage());
            Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            createAlertDialog("Registration error",""+notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
        }
    }
}
