package com.nearbypets.activities;

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

public class RegisterActivity extends AppCompatActivity {
  private  EditText mRegisterEmailId, mRegisterFirstName, mRegisterLastName,
            mRegisterPassword, mRegisterPhoneNumber;
  private  Button mRegister;

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

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailString = mRegisterEmailId.getText().toString();
                if(mRegisterFirstName.getText().toString().trim().length()==0 )
                {
                    mRegisterFirstName.setError("Please Provide First Name");

                }else if(mRegisterFirstName.getText().toString().trim().length()<2 | mRegisterFirstName.getText().toString().trim().length() > 30)
                {
                    mRegisterFirstName.setError("First Name should have mini 2 and max 30 letters");

                }
                if(mRegisterLastName.getText().toString().trim().length() ==0)
                {
                    mRegisterLastName.setError("Please Provide Last Name");

                }else if(mRegisterLastName.getText().toString().trim().length()<2 | mRegisterLastName.getText().toString().trim().length() > 30)
                {
                    mRegisterLastName.setError("Last Name should have mini 2 and max 30 letters");

                }
                if(!isValidEmail(emailString))
                {
                    mRegisterEmailId.setError("Please Provide Proper email Id");

                }if(mRegisterPassword.getText().toString().trim().length() ==0)
                {
                    mRegisterPassword.setError("Please Provide Password");
                }else if(mRegisterPassword.getText().toString().trim().length() < 4 |mRegisterPassword.getText().toString().trim().length() > 20)
                {
                    mRegisterPassword.setError("Last Name should have mini 4 and max 20 letters");
                }

                if(mRegisterPhoneNumber.getText().toString().trim().length()>=10)
                {
                    mRegisterPhoneNumber.setError("Phone Number must have 10 digit");

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
