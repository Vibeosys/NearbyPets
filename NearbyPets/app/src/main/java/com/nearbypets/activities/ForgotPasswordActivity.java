package com.nearbypets.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.nearbypets.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText mEmailId;
    private Button resendPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();
        setTitle(getResources().getString(R.string.activity_forgot_pass));
        mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        resendPass = (Button) findViewById(R.id.resend_pass);
        mEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        resendPass.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));

        resendPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = mEmailId.getText().toString().trim();
                if(!isValidEmail(emailId))
                {
                    mEmailId.requestFocus();
                    mEmailId.setError("Please provide  email Id");

                }else
                {

                    //add logic to send an email- Email will be sent to the customer with clear text password.

                    Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(loginActivity);
                }

            }
        });
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
