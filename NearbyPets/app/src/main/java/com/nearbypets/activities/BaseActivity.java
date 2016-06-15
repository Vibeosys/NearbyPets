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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.utils.ServerSyncManager;
import com.nearbypets.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

/**
 * Base Activity will give the basic implementation with async task support and other things
 */
public abstract class BaseActivity
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    protected ServerSyncManager mServerSyncManager = null;
    protected static SessionManager mSessionManager = null;
    protected final static String TAG = "com.nearbypets";
    protected LocationManager mLocationManager = null;
    protected GoogleApiClient mGoogleApiClient = null;
    protected Location mLocation = null;
    protected LocationRequest mLocationRequest = null;
    protected double currentLat;
    protected double currentLong;
    //protected Tracker mTracker;
    protected static HashMap<String, String> settingMap = new HashMap<>();
    protected static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    protected static final int PERMISSION_REQUEST_CAMERA = 2;
    protected static final int PERMISSION_REQUEST_READ_WRITE_STORAGE = 3;
    protected static final int PERMISSION_REQUEST_MEDIA_TYPE_IMAGE = 11;
    protected static final int PERMISSION_REQUEST_MEDIA_TYPE_SECOND_IMAGE = 21;
    protected static final int PERMISSION_REQUEST_MEDIA_TYPE_THIRD_IMAGE = 31;
    protected static final int PERMISSION_REQUEST_CALL_PHONE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance(getApplicationContext());
        mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        settingMap.put("ClassifiedAdPageSize", "10");
        settingMap.put("FacebookAdPageSize", "5");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

            if (hideFormView != null) {
                hideFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                hideFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }
            if (showProgressView != null) {
                showProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                showProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
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
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            //GPS is not enabled !!
            return;
        }
        //Location lastKnownLocation = null;
        List<String> providers = null;
        //if (locationManager != null)
        providers = locationManager.getAllProviders();

        if (providers != null) {
            //for (int i = 0; i < providers.size(); i++) {
            //if (locationManager != null)

            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d("Permissions", "Permission has been granted");
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
                requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
            }
                /*lastKnownLocation = locationManager.getLastKnownLocation(providers.get(i));
                if (lastKnownLocation != null) {
                    currentLat = lastKnownLocation.getLatitude();
                    currentLong = lastKnownLocation.getLongitude();
                    break;
                }*/
        }
        //}
    }

    protected boolean checkPermission(String strPermission) {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), strPermission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestPermission(String strPermission, int perCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, strPermission)) {
            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. " +
                    "Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{strPermission}, perCode);
        }
    }

    protected void getPermissionsForReadWriteStorage(int requestCode) {
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d("Permissions", "Permission has been granted");
            performReadWriteStorageAction(requestCode);
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, requestCode);
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode);
        }
    }

    protected void getPermissionsForCamera(int requestCode) {
        if (checkPermission(Manifest.permission.CAMERA)
                && checkPermission(Manifest.permission.MEDIA_CONTENT_CONTROL)) {
            Log.d("Permissions", "Permission has been granted");
            captureImage();
            //fetchLocationData();
        } else {
            requestPermission(Manifest.permission.CAMERA, requestCode);
            requestPermission(Manifest.permission.MEDIA_CONTENT_CONTROL, requestCode);
        }
    }

    protected void captureImage() {
    }

    protected void getPermissionsForPhoneCall() {
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            Log.d("Permissions", "Permission has been granted");
            callToPhone();
            //captureImage();
            //fetchLocationData();
        } else {
            requestPermission(Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CALL_PHONE);
        }
    }

    protected void callToPhone() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocationData();
                    break;
                }
            case PERMISSION_REQUEST_MEDIA_TYPE_IMAGE:
            case PERMISSION_REQUEST_MEDIA_TYPE_SECOND_IMAGE:
            case PERMISSION_REQUEST_MEDIA_TYPE_THIRD_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performReadWriteStorageAction(requestCode);
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    captureImage();
                }
                break;
            case PERMISSION_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callToPhone();
                }
                break;
        }
    }

    protected void performReadWriteStorageAction(int requestCode) {
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Log.d("Permissions", "Permission has been granted");
            //fetchLocationData();

            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                startLocationUpdates();
            }
            if (mLocation != null) {
                currentLat = mLocation.getLatitude();
                currentLong = mLocation.getLongitude();
            } else {
                Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }

    protected void fetchLocationData() {
        Location lastKnownLocation = null;
        List<String> providers = mLocationManager.getAllProviders();
        for (int i = 0; i < providers.size(); i++) {
            String provider = providers.get(i);
            lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null) {
                currentLat = lastKnownLocation.getLatitude();
                currentLong = lastKnownLocation.getLongitude();
            }
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);
        // Request location updates
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
            Log.d("reque", "--->>>>");
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
