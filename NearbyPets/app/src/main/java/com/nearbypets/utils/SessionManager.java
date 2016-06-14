package com.nearbypets.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Class helps to retrive or set values from shared preferences.
 * In case there are no values, it feeds in values from app.properties from assets directory
 * Created by anand on 29-10-2015.
 */
public class SessionManager {
    private static final String PROJECT_PREFERENCES = "com.nearbypets";

    private static SessionManager mSessionManager;
    private static SharedPreferences mProjectSharedPref = null;
    private static Context mContext = null;
    private static PropertyFileReader mPropertyFileReader = null;

    /**
     * Method is supposed to called once at beginning of the APP.
     *
     * @param context Context of any base or current activity, Needs to be called at only once.
     * @return SessionManager session manager instance
     */

    public static SessionManager getInstance(Context context) {
        if (mSessionManager != null)
            return mSessionManager;

        mContext = context;
        mPropertyFileReader = PropertyFileReader.getInstance(context);
        loadProjectSharedPreferences();
        mSessionManager = new SessionManager();

        return mSessionManager;
    }

    /**
     * Gets singleton instance of session manager class
     *
     * @return Singleton Instance of Session Manager
     */

    public static SessionManager Instance() {
        if (mSessionManager != null)
            return mSessionManager;
        else
            throw new IllegalArgumentException("No instance is yet created");
    }

    /**
     * Loads all the App.Properties file values into shared preferences.
     * In case of version upgrade, replaces all the values in the shared preferences.
     */

    private static void loadProjectSharedPreferences() {
        if (mProjectSharedPref == null) {
            mProjectSharedPref = mContext.getSharedPreferences(PROJECT_PREFERENCES, Context.MODE_PRIVATE);
        }

        setValuesInSharedPrefs(PropertyTypeConstants.API_ENDPOINT_URI, mPropertyFileReader.getEndPointUri());
    }

    private SessionManager() {
    }

    public String getEndpointUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.API_ENDPOINT_URI, null);
    }

    public void setEndpointUrl(String endpointUrl) {
        setValuesInSharedPrefs(PropertyTypeConstants.API_ENDPOINT_URI, endpointUrl);
    }


    public String getUserId() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_ID, null);
    }

    public void setUserId(String userId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ID, userId);
    }

    public String getUserName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_NAME, null);
    }

    public void setUserName(String firstName, String lastName) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_NAME, firstName + " " + lastName);
    }

    public String getUserEmailId() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_EMAIL_ID, null);
    }

    public void setUserEmailId(String userEmailId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_EMAIL_ID, userEmailId);
    }

    public String getUserAccessToken() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_FB_ACCESS_TOKEN, null);
    }

    public void setUserAccessToken(String userAccessToken) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_FB_ACCESS_TOKEN, userAccessToken);
    }

    public String getUserPassword() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_PASSWORD, null);
    }

    public void setUserPassword(String userPassword) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_PASSWORD, userPassword);
    }

    public void setUserRoleId(int roleId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ROLE_ID, String.valueOf(roleId));
    }

    public int getUserRoleId() {
        String sRoleId = mProjectSharedPref.getString(PropertyTypeConstants.USER_ROLE_ID, null);
        if (sRoleId == null || TextUtils.isEmpty(sRoleId))
            return 0;
        else
            return Integer.parseInt(sRoleId);
    }

    public void setRadiusInKm(int radiusInKm) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_RADIUS_IN_KM, String.valueOf(radiusInKm));
    }

    public int getRadiusInKm() {
        String sRoleId = mProjectSharedPref.getString(PropertyTypeConstants.USER_RADIUS_IN_KM, null);
        if (sRoleId == null || TextUtils.isEmpty(sRoleId))
            return 5000;
        else
            return Integer.parseInt(sRoleId);
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }
}
