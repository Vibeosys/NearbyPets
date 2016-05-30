package com.nearbypets.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.nearbypets.R;
import com.nearbypets.views.MyriadProRegularTextView;

public class LoginActivity extends AppCompatActivity {
    EditText mEmailId,mPassword;
    MyriadProRegularTextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button signIn = (Button) findViewById(R.id.login_user);
        EditText mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        EditText mPassword =(EditText) findViewById(R.id.user_password_editText);
        forgot_password = (MyriadProRegularTextView)findViewById(R.id.forgot_password_textview);
        mEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        mPassword.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        signIn.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/MyriadPro-Regular.otf"));
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot =new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(forgot);
            }
        });
    }

    /*protected void callRegister(View v) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }*/
}
