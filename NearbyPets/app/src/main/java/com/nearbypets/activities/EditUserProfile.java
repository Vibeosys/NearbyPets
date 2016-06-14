package com.nearbypets.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nearbypets.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserProfile extends BaseActivity {

    private EditText mFirstName,mLastName,mEmailId,mPhoneNumber,mPasswordEdit;
    String mName,mEmail,mPhone,mLastN,mPassword;
    private Button mBtnSave,mBtnCancel;
    private View formView;
    private View progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        mFirstName = (EditText) findViewById(R.id.firstNameTv);
        mEmailId = (EditText)findViewById(R.id.EmailIdTv);
        mPhoneNumber = (EditText) findViewById(R.id.PhoneTv);
        mLastName = (EditText) findViewById(R.id.lastNameTv);
        mPasswordEdit =(EditText) findViewById(R.id.PassWdTv);
        mBtnSave = (Button) findViewById(R.id.btnSave);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        formView = findViewById(R.id.profileLinear);
        progressBar = findViewById(R.id.progressBar);
        mPassword = mSessionManager.getUserPassword();
        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras!=null)
            {
                mName = extras.getString("FirstName");
                mLastN= extras.getString("LastName");
                mEmail=extras.getString("EmailId");
                mPhone = extras.getString("Phone");
                mFirstName.setText(""+mName);
                mEmailId.setText(""+mEmail);
                mPhoneNumber.setText(""+mPhone);
                mLastName.setText(""+mLastN);
                mPasswordEdit.setText(""+mPassword);
            }
        }else {

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
                switch (id)
                {
                    case R.id.btnSave:
                        boolean cancelFlag = false;
                        View focusView = null;
                        String firstNameStr = mFirstName.getText().toString().trim();
                        String lastNameStr = mLastName.getText().toString().trim();
                        String emailStr = mEmailId.getText().toString().trim();
                        String password = mPasswordEdit.getText().toString().trim();
                        if (TextUtils.isEmpty(firstNameStr)) {
                            focusView = mFirstName;
                            mFirstName.setError("Please provide first name");
                            cancelFlag = true;

                        }else if (firstNameStr.length() < 2) {
                            focusView = mFirstName;
                            mFirstName.setError("First Name should have atleast 2 character");
                            cancelFlag = true;
                        } else if (firstNameStr.length() > 30) {
                            focusView = mFirstName;
                            mFirstName.setError("Maximum 30 characters are allowed");
                            cancelFlag = true;

                        }
                        else if (TextUtils.isEmpty(lastNameStr))

                        {
                            focusView = mLastName;
                            mLastName.setError("Please provide Last name");
                            cancelFlag = true;
                        }
                        else if (lastNameStr.length() < 2) {
                            focusView = mLastName;
                            mLastName.setError("Last Name should have atleast 2 character");
                            cancelFlag = true;
                        } else if (lastNameStr.length() > 30) {
                            focusView = mLastName;
                            mLastName.setError("Maximum 30 characters are allowed");
                            cancelFlag = true;

                        }
                        if (isValidEmail(emailStr)) {
                            focusView = mEmailId;
                            mEmailId.setError("Please Provide proper email id");
                            cancelFlag = true;
                        }else if(TextUtils.isEmpty(emailStr))
                        {
                            focusView = mEmailId;
                            mEmailId.setError("Email Id cannot be blank");
                            cancelFlag = true;
                        }
                        else if (password.length() < 4) {
                            focusView = mPasswordEdit;
                            mPasswordEdit.setError("Password should have minimum 4 character");
                            cancelFlag = true;
                        } else if (password.length() > 20) {
                            focusView = mPasswordEdit;
                            mPasswordEdit.setError("Maximium 20 characters are allowed");
                            cancelFlag = true;

                        }else if(TextUtils.isEmpty(password))
                        {
                            focusView = mPasswordEdit;
                            mPasswordEdit.setError("Password cannot be blank");
                            cancelFlag = true;
                        }
                        else
                        {
                            CallToUpdateRegister();
                        }
                }
            }




        });

    }
    private void CallToUpdateRegister() {

        showProgress(true, formView, progressBar);

    }
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
