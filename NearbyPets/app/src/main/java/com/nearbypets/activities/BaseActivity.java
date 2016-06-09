package com.nearbypets.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nearbypets.data.SettingsDTO;
import com.nearbypets.utils.EditTextValidation;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

/**
 * Base Activity will give the basic implementation with async task support and other things
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ServerSyncManager mServerSyncManager = null;
    protected static SessionManager mSessionManager = null;
    protected final static String TAG = "com.nearbypets";
    static EditTextValidation editTextValidation = null;
    protected LocationManager mLocationManager = null;
    protected double currentLat;
    protected double currentLong;
    //protected Tracker mTracker;
    protected static HashMap<String, String> settingMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance(getApplicationContext());
        mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        settingMap.put("AdSearchDistanceInKM", "10000");
        settingMap.put("ClassifiedAdPageSize", "10");
        settingMap.put("FacebookAdPageSize", "5");
        editTextValidation = new EditTextValidation();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    protected void updateSettings(List<SettingsDTO> settings) {
        //Add logic to update settings
        for (SettingsDTO setting : settings
                ) {
            settingMap.put(setting.getConfigKey(), setting.getConfigValue());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void createAlertNetWorkDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // whatever...
                        dialog.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                    }
                }).create().show();
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

    public void getCurrentLocation(LocationManager locationManager) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createAlertDialog("Allow Location Tracking", "Please allow the app to track your location");
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS) );
            //GPS is not enabled !!
            return;
        }
        Location lastKnownLocation = null;
        List<String> providers = null;
        if (locationManager != null) providers = locationManager.getAllProviders();

        if (providers != null) {
            for (int i = 0; i < providers.size(); i++) {
                if (locationManager != null)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                lastKnownLocation = locationManager.getLastKnownLocation(providers.get(i));
                if (lastKnownLocation != null) {
                    currentLat = lastKnownLocation.getLatitude();
                    currentLong = lastKnownLocation.getLongitude();
                    break;
                }
            }
        }
    }

}
