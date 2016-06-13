package com.nearbypets.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.nearbypets.MainActivity;
import com.nearbypets.R;
import com.nearbypets.data.LoginDBDTO;
import com.nearbypets.data.RegisterDBDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.data.downloaddto.UserDbDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.EditTextValidation;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.UserAuth;
import com.nearbypets.views.MyriadProRegularTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {

    private EditText mEmailId, mPassword;
    private CallbackManager callbackManager;
    private final int REQ_TOKEN_LOGIN = 1;
    private final int REQ_TOKEN_REGISTER = 2;
    private View formView;
    private View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();
        getCurrentLocation(mLocationManager);
        //Button signIn = (Button) findViewById(R.id.login_user);
        mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        mPassword = (EditText) findViewById(R.id.user_password_editText);
        MyriadProRegularTextView create_account = (MyriadProRegularTextView) findViewById(R.id.create_account_text);
        Button loginBtn = (Button) findViewById(R.id.login_user);
        formView = findViewById(R.id.formLogin);
        progressBar = findViewById(R.id.progressBar);
        LoginButton btnFbLogin = (LoginButton) findViewById(R.id.connectWithFbButton);
        //Context context = this.getApplicationContext();
        MyriadProRegularTextView forgot_password = (MyriadProRegularTextView) findViewById(R.id.forgot_password_textview);

        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check network connection");
        }

        mEmailId.setFocusable(false);
        mEmailId.setFocusableInTouchMode(true);

        loginBtn.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(forgot);
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regist = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regist);
            }
        });
        //Facebook Code
        btnFbLogin.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
                                    String FbCurrentToken = null;
                                    if (fbAccessToken != null) {
                                        //String FbAppId = Fbtoken.getApplicationId();
                                        FbCurrentToken = fbAccessToken.getToken();
                                        fbAccessToken.isExpired();
                                    }

                                    callToRegister(firstName, lastName, email, FbCurrentToken);
                                    mSessionManager.setUserAccessToken(FbCurrentToken);
                                    Log.d(TAG, "## email" + email + " first Name" + firstName + " lastname " + lastName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void callToRegister(String fname, String lname, String email, String accessTokan) {
        showProgress(true, formView, progressBar);
        RegisterDBDTO register = new RegisterDBDTO(fname, lname, email, accessTokan, 2);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(register);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_REGISTER, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_REGISTER, tableDataDTO);
    }

    private void callToLogin() {
        showProgress(true, formView, progressBar);
        LoginDBDTO login = new LoginDBDTO(mEmailId.getText().toString(), mPassword.getText().toString());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(login);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_LOGIN, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_LOGIN, tableDataDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_LOGIN: //error on Login
                showProgress(false, formView, progressBar);
                Log.d("TAG", "##" + error.toString());
                break;
            case REQ_TOKEN_REGISTER:
                showProgress(false, formView, progressBar);
                createAlertDialog("Server error!!!", "Try Again Later");
                Log.d("Error", "##REQ" + error.toString());
                break;
        }

    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        showProgress(false, formView, progressBar);
        createAlertDialog("Login error", "" + errorDbDTO.getMessage());
        Log.i("TAG", "##" + errorDbDTO.getMessage());

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        UserDbDTO userDbDTO = UserDbDTO.deserializeJson(data);
        UserAuth userAuth = new UserAuth();
        userAuth.saveAuthenticationInfo(userDbDTO, getApplicationContext());
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        showProgress(false, formView, progressBar);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LoginManager.getInstance().logOut();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.login_user:

                boolean cancelFlag = false;
                View focusView = null;
                String emailStr = mEmailId.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                mEmailId.setError(null);
                mPassword.setError(null);
                if (TextUtils.isEmpty(emailStr) || !EditTextValidation.isValidEmail(emailStr)) {
                    focusView = mEmailId;
                    mEmailId.setError("Please provide a valid email Id");
                    cancelFlag = true;
                }
                if (TextUtils.isEmpty(password)) {
                    focusView = mPassword;
                    mPassword.setError("Please provide password");
                    cancelFlag = true;
                }
                if (cancelFlag) {
                    focusView.requestFocus();
                } else {
                    callToLogin();
                    /*Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainScreen);*/
                }
                break;
        }
    }


}
