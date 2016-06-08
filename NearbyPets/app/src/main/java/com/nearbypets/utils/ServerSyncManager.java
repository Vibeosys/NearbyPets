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
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.Upload;
import com.nearbypets.data.UploadUser;
import com.nearbypets.data.downloaddto.DownloadDataDbDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.interfaces.BackgroundTaskCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anand on 09-01-2016.
 */
public class ServerSyncManager
        implements BackgroundTaskCallback {

    //private DbRepository mDbRepository;
    private SessionManager mSessionManager;
    private Context mContext;
    //private boolean mIsWorkInProgress;
    //private OnDownloadReceived mOnDownloadReceived;
    private OnSuccessResultReceived mOnSuccessResultReceived;
    private OnErrorResultReceived mErrorReceived;
    private OnSuccessResultReceived mOnSuccessResultSettingsReceived;
    private String TAG = ServerSyncManager.class.getSimpleName();
    //private OnNotifyUser mNotifyUser;

    public ServerSyncManager() {

    }

    public ServerSyncManager(@NonNull Context context, @NonNull SessionManager sessionManager) {
        mContext = context;
        mSessionManager = sessionManager;
    }

    public void uploadDataToServer(int requestTokan, TableDataDTO... params) {
        if (params == null || params.length <= 0) {
            Log.e("UploadNoData", "No data for upload was given by the respective method");
            return;
        }

        final ProgressDialog progress = new ProgressDialog(mContext);
        /*if (mSessionManager.getUserId() == 0 || mSessionManager.getUserName() == null
                || mSessionManager.getUserName().isEmpty()) {
            Log.e("UserNotAuth", "User is not authenticated before upload");
            return;
        }*/
        String uploadJson = prepareUploadJsonFromData(params);
        String uploadURL = mSessionManager.getEndpointUrl();
        Log.i(TAG, "##" + uploadURL);
        uploadJsonToServer(uploadJson, uploadURL, progress, requestTokan);
    }

    public void setOnStringResultReceived(OnSuccessResultReceived stringResultReceived) {
        mOnSuccessResultReceived = stringResultReceived;
    }

    public void setOnStringErrorReceived(OnErrorResultReceived stringErrorReceived) {
        mErrorReceived = stringErrorReceived;
    }

    private String prepareUploadJsonFromData(TableDataDTO... params) {

        Upload uploadToServer = new Upload();
        uploadToServer.setUser(new UploadUser(mSessionManager.getUserId(), mSessionManager.getUserEmailId(),
                mSessionManager.getUserPassword(), mSessionManager.getUserAccessToken()));
        uploadToServer.setData(Arrays.asList(params));
        String uploadJson = uploadToServer.serializeString();
        Log.i(TAG, "## request json" + uploadJson);
        return uploadJson;
    }

    private void uploadJsonToServer(String uploadJson, String uploadUrl, final ProgressDialog progress,
                                    final int requestToken) {
        RequestQueue vollyRequest = Volley.newRequestQueue(mContext);

        JsonObjectRequest uploadRequest = new JsonObjectRequest(Request.Method.POST,
                uploadUrl, uploadJson, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Upload Response", "" + response.toString());
                DownloadDataDbDTO downloadDataDbDTO = DownloadDataDbDTO.deserializeJson(response.toString());
                if (downloadDataDbDTO == null) {
                    if (mErrorReceived != null)
                        mErrorReceived.onDataErrorReceived(downloadDataDbDTO.getError(), requestToken);
                    Log.e("Data Error", "Error to get the data");
                    return;
                }

                if (downloadDataDbDTO.getError().getErrorCode() > 0) {
                    if (mErrorReceived != null)
                        mErrorReceived.onDataErrorReceived(downloadDataDbDTO.getError(), requestToken);
                    Log.e("Data Error", "Error to get the data");
                    return;
                }

                if (mOnSuccessResultReceived != null) {
                    mOnSuccessResultReceived.onResultReceived(downloadDataDbDTO.getData(), requestToken);
                    mOnSuccessResultSettingsReceived.onResultReceived(downloadDataDbDTO.getData(), downloadDataDbDTO.getSettings(), requestToken);
                }
                if (progress != null)
                    progress.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progress != null)
                    progress.dismiss();
                if (mErrorReceived != null)
                    mErrorReceived.onVolleyErrorReceived(error, requestToken);
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

    public interface OnSuccessResultReceived {
        void onResultReceived(@NonNull String data, int requestToken);

        void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken);
    }


    public interface OnErrorResultReceived {
        void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken);

        void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken);
    }

}
