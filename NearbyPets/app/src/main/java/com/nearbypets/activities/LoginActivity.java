package com.nearbypets.activities;


import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
;

;
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

public class LoginActivity extends  AppCompatActivity {
    EditText mEmailId,mPassword;
    MyriadProRegularTextView forgot_password;
    MyriadProRegularTextView create_account;
    Button loginBtn ;
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
   private static Context context;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile =Profile.getCurrentProfile();
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
        Button signIn = (Button) findViewById(R.id.login_user);
        EditText mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        EditText mPassword =(EditText) findViewById(R.id.user_password_editText);
        create_account = (MyriadProRegularTextView)findViewById(R.id.create_account_text);
        loginBtn =(Button) findViewById(R.id.login_user);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainScreen = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainScreen);
            }
        });
        context = this.getApplicationContext();
        forgot_password = (MyriadProRegularTextView)findViewById(R.id.forgot_password_textview);
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
                Intent forgot =new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(forgot);
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regist = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(regist);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void displayMessage(Profile profile) {
        if(profile !=null)
        {
            Toast.makeText(getApplicationContext(),"FB login",Toast.LENGTH_LONG).show();
            Log.d("FBLOGIN",profile.toString());
        }

    }
    private void LogOutDisplay() {

        {
            Toast.makeText(getApplicationContext(),"FB LOGOUT",Toast.LENGTH_LONG).show();
            Log.d("FBLOGIN","Logout");
        }

    }
    public static void LogoutFacebook()
    {
        LoginManager.getInstance().logOut();
       Log.d("FBLOGIN","Log out");
        /*Intent logout = new Intent(context, MainActivity.class);
        context.startActivity(logout);*/
    }
}
