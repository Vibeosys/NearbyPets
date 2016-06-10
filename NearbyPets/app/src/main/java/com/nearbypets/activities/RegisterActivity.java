package com.nearbypets.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.UserDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EditTextValidation;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    private EditText mRegisterEmailId, mRegisterFirstName, mRegisterLastName,
            mRegisterPassword, mRegisterPhoneNumber;
    private Button mRegister;
    private final int REQ_TOKEN_REGISTER = 2;
    private View formView;
    private View progressBar;
    EditTextValidation editTextValidation;

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
        formView = findViewById(R.id.formRegister);
        progressBar = findViewById(R.id.progressBar);
        mRegisterFirstName.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterLastName.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterPhoneNumber.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        mRegisterPassword.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));

        if (!NetworkUtils.isActiveNetworkAvailable(this)) {

            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");


        }


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


                String firstNameStr = mRegisterFirstName.getText().toString().trim();
                String lastNameStr = mRegisterLastName.getText().toString().trim();
                String emailStr = mRegisterEmailId.getText().toString().trim();
                String password = mRegisterPassword.getText().toString().trim();


                if (TextUtils.isEmpty(firstNameStr)) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("Please provide first name");
                    cancelFlag = true;

                } else if (firstNameStr.length() < 2) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("First Name should have atleast 2 character");
                    cancelFlag = true;
                } else if (firstNameStr.length() > 30) {
                    focusView = mRegisterFirstName;
                    mRegisterFirstName.setError("Maximum 30 characters are allowed");
                    cancelFlag = true;

                }
                if (TextUtils.isEmpty(lastNameStr))

                {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Please provide Last name");
                    cancelFlag = true;
                } else if (lastNameStr.length() < 2) {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Last Name should have atleast 2 character");
                    cancelFlag = true;
                } else if (lastNameStr.length() > 30) {
                    focusView = mRegisterLastName;
                    mRegisterLastName.setError("Maximum 30 characters are allowed");
                    cancelFlag = true;

                }
                if (  TextUtils.isEmpty(emailStr)) {
                    focusView = mRegisterEmailId;
                    mRegisterEmailId.setError("Please Provide proper email id");
                    cancelFlag = true;
                }
                if (TextUtils.isEmpty(password)) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Password field cannot be blank");
                    cancelFlag = true;
                } else if (password.length() < 4) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Password should have minimum 4 character");
                    cancelFlag = true;
                } else if (password.length() > 20) {
                    focusView = mRegisterPassword;
                    mRegisterPassword.setError("Maximium 20 characters are allowed");
                    cancelFlag = true;

                }
                if (cancelFlag) {
                    focusView.requestFocus();
                } else {
                    callToRegister();

                }
        }

    }

    public void callToRegister() {
        showProgress(true, formView, progressBar);
        RegisterDBDTO register = new RegisterDBDTO(mRegisterFirstName.getText().toString(),
                mRegisterLastName.getText().toString(), mRegisterEmailId.getText().toString(),
                mRegisterPassword.getText().toString(), mRegisterPhoneNumber.getText().toString(), 1);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(register);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_REGISTER, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_REGISTER, tableDataDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_REGISTER:
                showProgress(false, formView, progressBar);
                createAlertDialog("Server error!!!", "Try Again Later");
                Log.d("Error", "##REQ" + error.toString());
                break;
        }

    }





   /* private void checkRegistration(ArrayList<NotificationDTO> notificationDTOs) {

        NotificationDTO notificationDTO = notificationDTOs.get(0);
        if (notificationDTO.getErrorCode() == 0) {
            Log.i("TAG", "##" + notificationDTO.getMessage());
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        } else {
            createAlertDialog("Registration error", "" + notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
        }
    }*/

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

        showProgress(false, formView, progressBar);
        Log.d("RESULT", "##REQ" + data.toString());
        try {
            UserDbDTO registerUser = UserDbDTO.deserializeJson(data);
            Toast toast= Toast.makeText(getApplicationContext(),"Register Sucessfully",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginIntent);
            Log.i(TAG, registerUser.toString());

        } catch (JsonSyntaxException e) {
            Log.e(TAG, "## error on response" + e.toString());
        }


    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        updateSettings(settings);
    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        if (errorDbDTO.getErrorCode() == 0) {
            showProgress(false, formView, progressBar);
            Log.i("TAG", "##" + errorDbDTO.getMessage());
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        } else {
            showProgress(false, formView, progressBar);
            createAlertDialog("Registration error", "" + errorDbDTO.getMessage());
            Log.i("TAG", "##" + errorDbDTO.getMessage());
        }

    }
}
