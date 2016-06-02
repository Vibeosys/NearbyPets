package com.nearbypets.activities;


import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nearbypets.MainActivity;
import com.nearbypets.R;
import com.nearbypets.views.MyriadProRegularTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailId, mPassword;
    private MyriadProRegularTextView forgot_password;
    private MyriadProRegularTextView create_account;
    private Button loginBtn, signIn;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private static Context context;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

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


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailStr = mEmailId.getText().toString().trim();
                if (!isValidEmail(emailStr)) {
                    mEmailId.requestFocus();
                    mEmailId.setError("Please provide email Id");

                } else if (mPassword.getText().toString().trim().length() == 0) {
                    mPassword.requestFocus();
                    mPassword.setError("Please provide password");
                } else {
                    callToLogin();
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainScreen);
                }


            }
        });
        context = this.getApplicationContext();
        forgot_password = (MyriadProRegularTextView) findViewById(R.id.forgot_password_textview);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
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
        profileTracker.startTracking();
        mEmailId.setFocusable(false);
        mEmailId.setFocusableInTouchMode(true);
       /* mEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        mPassword.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        signIn.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
*/

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
        String url = "https://nearby-pets.appspot.com/loginuser";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emailid", mEmailId.getText().toString());
                params.put("password", mPassword.getText().toString());
                return params;
            }
        };
    }
}
