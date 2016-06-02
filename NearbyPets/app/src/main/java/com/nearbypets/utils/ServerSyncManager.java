package com.nearbypets.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.interfaces.BackgroundTaskCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 09-01-2016.
 */
public class ServerSyncManager
        implements BackgroundTaskCallback {

    //private DbRepository mDbRepository;
    private SessionManager mSessionManager;
    private Context mContext;
    private boolean mIsWorkInProgress;
    private OnDownloadReceived mOnDownloadReceived;
    private OnStringResultReceived mOnStringResultReceived;
    private OnStringErrorReceived mErrorReceived;
    private String TAG = ServerSyncManager.class.getSimpleName();
    private OnNotifyUser mNotifyUser;

    public ServerSyncManager() {

    }

    public ServerSyncManager(@NonNull Context context, @NonNull SessionManager sessionManager) {
        mContext = context;
        mSessionManager = sessionManager;
        // mDbRepository = new DbRepository(mContext, mSessionManager);
    }

    public void syncDataWithServer(final boolean aShowProgressDlg) {
        Log.d("BaseActivity", "IN Base");
        // String downloadUrl = mSessionManager.getDownloadUrl(mSessionManager.getUserId(), mSessionManager.getUserRestaurantId());
        //String uploadJson = getUploadSyncJson();
        /*SyncDataDTO syncData = new SyncDataDTO();
        syncData.setDownloadUrl(downloadUrl);
        syncData.setUploadUrl(mSessionManager.getUploadUrl());
        syncData.setUploadJson(uploadJson);*/
        mIsWorkInProgress = true;
/*
        new BackgroundTask(aShowProgressDlg).execute(syncData);*/
    }

    public void uploadDataToServer(TableDataDTO... params) {
        if (params == null || params.length <= 0) {
            Log.e("UploadNoData", "No data for upload was given by the respective method");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(mContext);
        if (mSessionManager.getUserId() == 0 || mSessionManager.getUserName() == null
                || mSessionManager.getUserName().isEmpty()) {
            Log.e("UserNotAuth", "User is not authenticated before upload");
            return;
        }
        String uploadJson = prepareUploadJsonFromData(params);
        String uploadURL = mSessionManager.getUploadUrl();
        // Log.i(TAG, "##" + uploadJson);
        uploadJsonToServer(uploadJson, uploadURL, progress);
    }

    public boolean isDownloadInProgress() {
        return mIsWorkInProgress;
    }

    public void setOnDownloadReceived(OnDownloadReceived onDownloadReceived) {
        mOnDownloadReceived = onDownloadReceived;
    }

    public void setOnStringResultReceived(OnStringResultReceived stringResultReceived) {
        mOnStringResultReceived = stringResultReceived;
    }

    public void setOnStringErrorReceived(OnStringErrorReceived stringErrorReceived) {
        mErrorReceived = stringErrorReceived;
    }

    private String prepareUploadJsonFromData(TableDataDTO... params) {

        // Upload uploadToServer = new Upload();
        /*uploadToServer.setUser(new UploadUser(
                mSessionManager.getUserId(),
                mSessionManager.getUserRestaurantId(),
                mDbRepository.getPassword(mSessionManager.getUserId()), mSessionManager.getImei(), mSessionManager.getMac()));
       */
        // uploadToServer.setData(Arrays.asList(params));
        String uploadJson = "";// uploadToServer.serializeString();
        return uploadJson;
    }

    private void uploadJsonToServer(String uploadJson, String uploadUrl, final ProgressDialog progress) {
        RequestQueue vollyRequest = Volley.newRequestQueue(mContext);

        JsonObjectRequest uploadRequest = new JsonObjectRequest(Request.Method.POST,
                uploadUrl, uploadJson, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Upload Response", "" + response.toString());

                if (mOnStringResultReceived != null)
                    mOnStringResultReceived.onStingResultReceived(response);
                if (progress != null)
                    progress.dismiss();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progress != null)
                    progress.dismiss();
                if (mErrorReceived != null)
                    mErrorReceived.onStingErrorReceived(error);
                // Log.i(TAG, "##" + error.toString());
            }
        });
        uploadRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        vollyRequest.add(uploadRequest);
    }

    /*protected String getUploadSyncJson() {
        Upload uploadToServer = new Upload();
        *//*uploadToServer.setUser(new UploadUser(
                SessionManager.Instance().getUserId(), SessionManager.Instance().getUserRestaurantId()
                , mDbRepository.getPassword(mSessionManager.getUserId()), mSessionManager.getImei(), mSessionManager.getMac()));*//*
        List<Sync> syncRecordsInDb = mDbRepository.getPendingSyncRecords();
        ArrayList<TableDataDTO> tableDataList = new ArrayList<>();

        for (Sync syncEntry : syncRecordsInDb) {
            tableDataList.add(new TableDataDTO(syncEntry.getTableName(), syncEntry.getJsonSync()));
        }
        uploadToServer.setData(tableDataList);
        mDbRepository.clearSyncData();
        if (tableDataList.isEmpty()) {
            return null;
        } else {
            String uploadData = new Gson().toJson(uploadToServer);
            return uploadData;
        }
    }*/

    @Override
    public void onResultReceived(String downloadedJson) {

    }


    public interface OnDownloadReceived {
        void onDownloadResultReceived(@NonNull Map<String, Integer> results);
    }

    public interface OnStringResultReceived {
        void onStingResultReceived(@NonNull JSONObject data);
    }

    public interface OnStringErrorReceived {
        void onStingErrorReceived(@NonNull VolleyError error);
    }

    public interface OnNotifyUser {
        void onNotificationReceived(@NonNull String message);
    }

    public void setOnNotifyUser(OnNotifyUser notifyUser) {
        mNotifyUser = notifyUser;
    }
}
