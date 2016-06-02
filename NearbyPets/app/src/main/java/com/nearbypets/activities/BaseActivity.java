package com.nearbypets.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.nearbypets.utils.SessionManager;


/**
 * Created by akshay on 01-06-2016.
 */
public class BaseActivity extends AppCompatActivity {
    /* protected ServerSyncManager mServerSyncManager = null;
     protected DbRepository mDbRepository = null;*/
    protected static SessionManager mSessionManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mSessionManager = SessionManager.getInstance(getApplicationContext());
        /*mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        mDbRepository = new DbRepository(getApplicationContext(), mSessionManager);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
