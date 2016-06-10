package com.nearbypets.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nearbypets.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * Created by shrinivas on 10-06-2016.
 */
public class PreviewImage extends BaseActivity
        implements View.OnClickListener
         {

    private String imageData = null;
    private ProgressDialog mProgressDialog;
    //SessionManager mSessionManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewimage);
        setTitle("Preview Image");
        // Selected image id
        //newDataBase = new NewDataBase(getApplicationContext());
        final String path = getIntent().getExtras().getString("Data");
        Log.d("PreviewImage ImagePath", path);

        //Toast.makeText(getApplicationContext(), "" + path, Toast.LENGTH_SHORT).show();
        Button cancelButton = (Button) findViewById(R.id.cancelBtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button uploadImageButton = (Button) findViewById(R.id.uploadImageBtu);
        uploadImageButton.setOnClickListener(this);
        final ImageView imageView = (ImageView) findViewById(R.id.previewmyimage);

        imageView.post(new Runnable() {
            @Override
            public void run() {

                FileInputStream in;
                BufferedInputStream buf;
                Bitmap bMap;
                try {
                    in = new FileInputStream(path);
                    buf = new BufferedInputStream(in);
                    bMap = BitmapFactory.decodeStream(buf);
                    imageData = getStringImage(bMap);
                    imageView.setImageBitmap(bMap);
                    //bMap.recycle();
                    if (in != null) {
                        in.close();
                    }
                    if (buf != null) {
                        buf.close();
                    }
                } catch (Exception e) {
                    Log.e("ErrorDownLoad", e.toString());
                }

            }
        });
        /*bitmap = decodeURI(path);
        imageView.setImageBitmap(bitmap);*/
    }

    @Override
    public void onClick(View v) {


        final String imgLocalPath = getIntent().getExtras().getString("Data");
        Log.d("Path File ", imgLocalPath);




        mProgressDialog = new ProgressDialog(PreviewImage.this);
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();





    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
