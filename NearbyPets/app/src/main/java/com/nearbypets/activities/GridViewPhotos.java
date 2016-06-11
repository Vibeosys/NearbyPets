package com.nearbypets.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.nearbypets.R;
import com.nearbypets.utils.LoaderImageView;
import com.nearbypets.utils.SessionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shrinivas on 10-06-2016.
 */
public class GridViewPhotos extends BaseActivity {

    private Cursor cc = null;
    private ProgressDialog ProgressDialog = null;
    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private Uri imageUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "NearbyPetsPhotos";
    public static final int MEDIA_TYPE_IMAGE = 1;
    //private final int REQ_SELECT_IMAGE_REQUEST_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridviewphotos);
        setTitle("Choose Photos");
        mSessionManager = SessionManager.getInstance(getApplicationContext());
        GridView mGridViewPhotos = (GridView) findViewById(R.id.showgridphotos);

        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";
        cc = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                orderBy);
        if (cc != null) {

            if (cc.moveToFirst()) {
                ProgressDialog = new ProgressDialog(GridViewPhotos.this);
                ProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ProgressDialog.setMessage("Please Wait");
                //myProgressDialog.setIcon(R.drawable.blind);
                ProgressDialog.show();

                new Thread() {
                    public void run() {
                        try {
                            mUrls = new Uri[cc.getCount()];
                            strUrls = new String[cc.getCount()];
                            mNames = new String[cc.getCount()];
                            for (int i = 0; i < cc.getCount(); i++) {
                                cc.moveToPosition(i);
                                mUrls[i] = Uri.parse(cc.getString(1));
                                strUrls[i] = cc.getString(1);
                                mNames[i] = cc.getString(3);
                                //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ProgressDialog.dismiss();
                    }
                }.start();

            }

            GalleryAdapter theAdapter = new GalleryAdapter(this, cc);
            mGridViewPhotos.setAdapter(theAdapter);
            mGridViewPhotos.setOnItemClickListener(theAdapter);
            mGridViewPhotos.invalidate();

        }

    }


    private class GalleryAdapter extends CursorAdapter implements AdapterView.OnItemClickListener {

        public GalleryAdapter(Context context, Cursor aCur) {
            super(context, aCur, true);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Cursor cc = getCursor();
            cc.moveToPosition(position);
            Uri theUrl = Uri.parse(cc.getString(1));
            //String theName = cc.getString(3);
            View row = convertView;
            if (row == null) {
                LayoutInflater theLayoutInflator = getLayoutInflater();
                row = theLayoutInflator.inflate(R.layout.gridviewsource, null);
            }

            LoaderImageView image = (LoaderImageView) row.findViewById(R.id.viewImage);
            image.loadImageFromFile(theUrl.getPath());
            return row;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View row = null;
            LayoutInflater theLayoutInflator = getLayoutInflater();
            row = theLayoutInflator.inflate(R.layout.gridviewsource, null);
            //ViewHolder viewHolder = new ViewHolder();
            //row.setTag(viewHolder);
            return row;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long aId) {
            Cursor theCur = getCursor();
            theCur.moveToPosition(position);
            Uri theUri = Uri.parse(theCur.getString(1));
            startPostMyAdIntent(theUri.getPath());
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }

    private void startPostMyAdIntent(String imagePath) {
        Intent theIntent = new Intent(getApplicationContext(), PostMyAdActivity.class);
        theIntent.putExtra("imagePath", imagePath);
        setResult(RESULT_OK, theIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.get_photo) {
            //captureImage();
            getPermissionsForCamera(PERMISSION_REQUEST_CAMERA);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void captureImage() {
        Intent takephoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        takephoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takephoto, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private Uri getOutputMediaFileUri(int mediafile) {
        return Uri.fromFile(getOutputMediaFile(mediafile));

    }

    private File getOutputMediaFile(int mediafile) {

        File fileDIr;
        fileDIr = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!fileDIr.exists()) {
            if (!fileDIr.mkdir()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create"
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = null;
        if (mediafile == MEDIA_IMAGE)
            mediaFile = new File(fileDIr.getAbsolutePath(), "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.capture_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CAMERA_CAPTURE_IMAGE_REQUEST_CODE == requestCode) {
            startPostMyAdIntent(imageUri.getPath());
        }
    }
}
