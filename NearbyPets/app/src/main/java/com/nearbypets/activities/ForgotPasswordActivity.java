package com.nearbypets.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.downloaddto.ForgotDBDTO;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends BaseActivity implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived, View.OnClickListener  {
    private EditText mEmailId;
    private Button resendPass;
    private final int REQ_TOKEN_FORGOTPASS = 3;
   private View formView;
    private View progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().hide();
        setTitle(getResources().getString(R.string.activity_forgot_pass));
        mEmailId = (EditText) findViewById(R.id.user_email_id_editText);
        resendPass = (Button) findViewById(R.id.resend_pass);
        formView = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBarforgot);
        mEmailId.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));
        resendPass.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MyriadPro-Regular.otf"));

        if (!NetworkUtils.isActiveNetworkAvailable(this)) {

            createAlertNetWorkDialog("Network Error","Please check newtwork connection");


        }

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

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

                   callToForgotPassword();
                    //add logic to send an email- Email will be sent to the customer with clear text password.

                    Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(loginActivity);
                }

            }
        });
    }

    private void callToForgotPassword() {
        showProgress(true, formView, progressBar);
        ForgotDBDTO forgotPass = new ForgotDBDTO(mEmailId.getText().toString());
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(forgotPass);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.FORGOT_LOGIN, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_FORGOTPASS, tableDataDTO);
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

    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {
        switch (requestTokan)
        {
            case REQ_TOKEN_FORGOTPASS:
               showProgress(true, formView, progressBar);
                createAlertDialog("Server error!!!", "Try Again Later");
                Log.d("Error", "##REQ" + error.toString());
                break;
        }

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {
        switch(requestTokan)
        {
            case REQ_TOKEN_FORGOTPASS:
                showProgress(true, formView, progressBar);
                createAlertDialog("Succes Message!!!", "Go to login activity");

                Log.d("success", "##REQ" + data.toString());
                break;
        }

    }
}
