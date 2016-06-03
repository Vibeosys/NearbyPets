package com.nearbypets.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

        String versionNumber = mProjectSharedPref.getString(PropertyTypeConstants.VERSION_NUMBER, null);
        Float versionNoValue = versionNumber == null ? 0 : Float.valueOf(versionNumber);

        if (mPropertyFileReader.getVersion() > versionNoValue) {
            boolean sharedPrefChange = addOrUdateSharedPreferences();
            if (!sharedPrefChange)
                Log.e("SharedPref", "No shared preferences are changed");
        }
    }

    /**
     * Adds or updates entries into shared preferences.
     *
     * @return true or false based upon the update in shared preferences.
     */
    private static boolean addOrUdateSharedPreferences() {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        //editor.putString(PropertyTypeConstants.API_UPLOAD_IMAGE_URL, mPropertyFileReader.getImageUploadUrl());
        editor.putString(PropertyTypeConstants.VERSION_NUMBER, String.valueOf(mPropertyFileReader.getVersion()));
        editor.apply();
        return true;
    }


    private SessionManager() {
    }

    public int getUserId() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.USER_ID, 0);
    }

    public void setUserId(int userId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ID, userId);
    }
    public String getUploadUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.API_UPLOAD_URL, null);
    }
    public String getUserName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_NAME, null);
    }

    public void setUserName(String userName) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_NAME, userName);
    }

    public String getUserEmailId() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_EMAIL_ID, null);
    }

    public void setUserEmailId(String userEmailId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_EMAIL_ID, userEmailId);
    }

    public void setUserActive(boolean active) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_IS_ACTIVE, String.valueOf(active));
    }

    public boolean getUserActive() {
        return mProjectSharedPref.getBoolean(PropertyTypeConstants.USER_IS_ACTIVE, false);
    }

    public void setUserRollId(int rollId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ROLL_ID, rollId);
    }

    public int getUserRollId() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.USER_ROLL_ID, 0);
    }


    public void setUserPhotoUrl(String userPhotoUrl) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_PHOTO_URL, userPhotoUrl);
    }

    public String getUserPhotoUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_PHOTO_URL, null);
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, int sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putInt(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }
}