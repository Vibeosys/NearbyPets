package com.nearbypets.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import com.google.android.gms.common.ConnectionResult;

import com.google.gson.Gson;
import com.nearbypets.R;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Base Activity will give the basic implementation with async task support and other things
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    protected ServerSyncManager mServerSyncManager = null;
    // protected DbRepository mDbRepository = null;
    protected static SessionManager mSessionManager = null;
    protected final static String TAG = "com.nearbypets";
    //protected Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance(getApplicationContext());
        mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        //mDbRepository = new DbRepository(getApplicationContext(), mSessionManager);


    }

    protected void updateSettings(ArrayList<SettingsDTO> settings) {
        //Add logic to update settings
    }

    protected void downloadImgFromFbGPlusAndUploadToAws(final String url) {
        URL fbAvatarUrl = null;
        Bitmap thirdPartyImage = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            fbAvatarUrl = new URL(url);
            thirdPartyImage = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            Log.e("DownloadImageEx", "Exception while downloading AWS image " + e.toString());
        } catch (IOException e) {
            Log.e("DownloadImageEx", "Exception while downloading AWS image " + e.toString());
        }

        /*ImageFileUploader imageFileUploader = new ImageFileUploader(getApplicationContext());
        imageFileUploader.setOnUploadCompleteListener(this);
        imageFileUploader.setOnUploadErrorListener(this);
        imageFileUploader.uploadUserProfileImage(thirdPartyImage);*/
    }

    protected static synchronized void downloadImageAsync(final String url, final ImageView imageView) {
        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {

            @Override
            public Bitmap doInBackground(Void... params) {
                URL fbAvatarUrl = null;
                Bitmap fbAvatarBitmap = null;
                try {
                    fbAvatarUrl = new URL(url);
                    fbAvatarBitmap = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    Log.e("DownloadImgBkgErr", "TravelAppError occurred while downloading profile image in background " + e.toString());
                } catch (IOException e) {
                    Log.e("DownloadImgBkgErr", "TravelAppError occurred while downloading profile image in background " + e.toString());
                } catch (Exception e) {
                    Log.e("DownloadImgBkgErr", "TravelAppError occurred while downloading profile image in background " + e.toString());
                }
                return fbAvatarBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                if (result != null)
                    imageView.setImageBitmap(result);
            }

        };
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //@Override
    public void onUploadComplete(String uploadJsonResponse, Map<String, String> inputParameters) {
        try {
            JSONObject jsonObject = new JSONObject(uploadJsonResponse);
            String imageUrl = jsonObject.getString("message");
            SessionManager.Instance().setUserPhotoUrl(imageUrl);
        } catch (JSONException e) {
            Log.e("ProfileImgUpErr", "JSON exception while uploading the image." + e.toString());
        }

        this.finish();
    }

    //@Override
    public void onUploadError(VolleyError error) {

        Log.e("ProfileUploadError", error.toString());
        this.finish();
    }

    protected void createAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // whatever...
                    }
                }).create().show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show, final View hideFormView, final View showProgressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            hideFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            hideFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    hideFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            showProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            showProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    showProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            showProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            hideFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
/*
    protected void customAlterDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + title);
        builder.setIcon(R.drawable.ic_action_warning_yellow);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }
*/

   /* protected String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void showMyDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.show_network_alert);
        dialog.setTitle("Network " + getResources().getString(R.string.alert_dialog));
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
            }
        });
        dialog.show();
    }

    protected void hitActivity() {
        String screenName = getScreenName();
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

    protected abstract String getScreenName();

    private boolean isGooglePlayServicesAvailable(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        // Showing status
        if (status == ConnectionResult.SUCCESS)
            return true;
        else {
            return false;
        }
    }

    protected void sendEventToGoogle(String category, String action) {
        if (mTracker != null)
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());
        else
            Log.d(TAG, "Analytics is not stated");
    }

    private ArrayList<Integer> getPermissionSet() {
        ArrayList<Integer> permissions = new ArrayList<>();
        String strPermissions = mSessionManager.getUserPermission();
        String[] result = strPermissions.split("\\|");
        for (String s : result) {
            permissions.add(Integer.parseInt(s));
            Log.d(TAG, "##" + s);
        }
        return permissions;
    }

    protected boolean getPermissionStatus(int id) {
        boolean status = false;
        ArrayList<Integer> permissions = getPermissionSet();
        if (permissions.contains(id)) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public String getMacAddress() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public void addError(String screenName, String method, String desc) {
        Gson gson = new Gson();
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        ApplicationErrorDBDTO errorDBDTO = new ApplicationErrorDBDTO(screenName, method, desc, dateUtils.getLocalSQLCurrentDate(), dateUtils.getLocalCurrentTime());
        String serializedError = gson.toJson(errorDBDTO);
        mDbRepository.addDataToSync(ConstantOperations.ADD_APPLICATION_ERROR, "" + mSessionManager.getUserId(), serializedError);
    }*/
}
