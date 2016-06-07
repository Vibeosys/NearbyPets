package com.nearbypets.activities;


import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
;

;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.MainActivity;
import com.nearbypets.R;
import com.nearbypets.data.LoginDBDTO;
import com.nearbypets.data.RegisterDBDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.DownloadRegisterDbDTO;
import com.nearbypets.data.downloaddto.NotificationDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.UserAuth;
import com.nearbypets.views.MyriadProRegularTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived, View.OnClickListener {
    private EditText mEmailId, mPassword;
    private MyriadProRegularTextView forgot_password;
    private MyriadProRegularTextView create_account;
    private Button loginBtn, signIn;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private static Context context;
    private final int REQ_TOKEN_LOGIN = 1;
    private final int REQ_TOKEN_REGISTER = 2;
    private View formView;
    private View progressBar;
    private LoginButton btnFbLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();

        signIn = (Button) findViewById(R.id.login_user);
        mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        mPassword = (EditText) findViewById(R.id.user_password_editText);
        create_account = (MyriadProRegularTextView) findViewById(R.id.create_account_text);
        loginBtn = (Button) findViewById(R.id.login_user);
        formView = findViewById(R.id.formLogin);
        progressBar = findViewById(R.id.progressBar);
        btnFbLogin = (LoginButton) findViewById(R.id.connectWithFbButton);
        //btnFbLogin.setReadPermissions("email");
        context = this.getApplicationContext();
        forgot_password = (MyriadProRegularTextView) findViewById(R.id.forgot_password_textview);

        if (!NetworkUtils.isActiveNetworkAvailable(this)) {

            createAlertNetWorkDialog("Network Error","Please check newtwork connection");


        }
       /* accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayMessage(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();*/
        mEmailId.setFocusable(false);
        mEmailId.setFocusableInTouchMode(true);


       /* mEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        mPassword.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        signIn.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
*/
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
                                    String accessTokan = loginResult.getAccessToken().toString();
                                    callToRegister(firstName, lastName, email, accessTokan);
                                    mSessionManager.setUserAccessTokan(accessTokan);
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

    private void displayMessage(Profile profile) {
        if (profile != null) {
            Toast.makeText(getApplicationContext(), "FB login", Toast.LENGTH_LONG).show();
            Log.d("FBLOGIN", profile.toString());
        }

    }

    private void LogOutDisplay() {

        {
            Toast.makeText(getApplicationContext(), "FB LOGOUT", Toast.LENGTH_LONG).show();
            Log.d("FBLOGIN", "Logout");
        }

    }

    public void callToRegister(String fname, String lname, String email, String accessTokan) {
        showProgress(true, formView, progressBar);
        RegisterDBDTO register = new RegisterDBDTO(fname, lname, email, accessTokan, 2);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(register);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.USER_REGISTER, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_REGISTER, tableDataDTO);
    }

    public static void LogoutFacebook() {
        LoginManager.getInstance().logOut();
        Log.d("FBLOGIN", "Log out");
        /*Intent logout = new Intent(context, MainActivity.class);
        context.startActivity(logout);*/
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_LOGIN: //error on Login
                showProgress(false, formView, progressBar);
                Log.d("TAG", "##" + error.toString());
                break;
            case REQ_TOKEN_REGISTER:
                showProgress(true, formView, progressBar);
                createAlertDialog("Server error!!!", "Try Again Later");
                Log.d("Error", "##REQ" + error.toString());
                break;
        }

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {
        switch (requestTokan) {
            case REQ_TOKEN_LOGIN: //login authentication
                showProgress(false, formView, progressBar);
                Log.d("RESULT", "##REQ" + data.toString());
                try {
                    DownloadRegisterDbDTO download = new Gson().fromJson(data.toString(), DownloadRegisterDbDTO.class);
                    updateSettings(download.getSettings());
                    Log.i(TAG, download.toString());
                    checkLogin(download.getData());
                } catch (JsonSyntaxException e) {
                    Log.e(TAG, "## error on response" + e.toString());
                }
                break;
            case REQ_TOKEN_REGISTER:
                showProgress(false, formView, progressBar);
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

    private void checkLogin(ArrayList<NotificationDTO> data) {
        NotificationDTO notificationDTO = data.get(0);
        if (notificationDTO.getErrorCode() == 0) {
            Log.i("TAG", "##" + notificationDTO.getMessage());
            UserAuth userAuth = new UserAuth();
            userAuth.saveAuthenticationInfo(notificationDTO.getData(), getApplicationContext());
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            createAlertDialog("Login error", "" + notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
        }
    }

    private void checkRegistration(ArrayList<NotificationDTO> notificationDTOs) {

        NotificationDTO notificationDTO = notificationDTOs.get(0);
        if (notificationDTO.getErrorCode() == 0 || notificationDTO.getErrorCode() == 102) {
            Log.i("TAG", "##" + notificationDTO.getMessage());
            UserAuth userAuth = new UserAuth();
            userAuth.saveAuthenticationInfo(notificationDTO.getData(), getApplicationContext());
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            createAlertDialog("Login error", "" + notificationDTO.getMessage());
            Log.i("TAG", "##" + notificationDTO.getMessage());
        }
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
                if (!isValidEmail(emailStr) || TextUtils.isEmpty(emailStr)) {
                    focusView = mEmailId;
                    mEmailId.setError("Please provide email Id");
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
