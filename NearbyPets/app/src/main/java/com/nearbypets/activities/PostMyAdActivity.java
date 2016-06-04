package com.nearbypets.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nearbypets.R;
import com.nearbypets.utils.DecimalDigitsInputFilter;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostMyAdActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnStringErrorReceived,ServerSyncManager.OnStringResultReceived {

    private ArrayAdapter<String> mCategoryAdapter;
    private ArrayAdapter<String> mTypeAdapter;
    private Spinner spnCategory;
    private Spinner spnType;
    private Button postMyAdBtn;
    private EditText petTitle,petDecsription,petPrice;
    private ImageView firstimg,secondimg,thirdimg;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_SECOND_IMAGE = 2;
    public static final int MEDIA_TYPE_THIRD_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_add);
        setTitle(getResources().getString(R.string.activity_post_ad));
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnType = (Spinner) findViewById(R.id.spnType);
        postMyAdBtn = (Button) findViewById(R.id.postMyAdd);
        petTitle = (EditText)findViewById(R.id.pet_title) ;
        petDecsription = (EditText)findViewById(R.id.pet_description);
        petPrice = (EditText)findViewById(R.id.pet_price);
        firstimg = (ImageView) findViewById(R.id.firstImg);
        secondimg = (ImageView) findViewById(R.id.secondImg);
        thirdimg = (ImageView) findViewById(R.id.thirdImg) ;


        String[] category = {"Parrot", "Cats", "Dogs"};
        mCategoryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, category);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnCategory.setAdapter(mCategoryAdapter);

        String[] type = {"For Sale", "For Adoption", "Lost/Found"};
        mTypeAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.dropdown_list_item, type);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnType.setAdapter(mTypeAdapter);

        firstimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"first Image view is clicked",Toast.LENGTH_LONG).show();
                captureImage();
            }
        });
        secondimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"first Image view is clicked",Toast.LENGTH_LONG).show();
                captureSecondImage();
            }
        });
        thirdimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureThirdImage();
            }
        });
        postMyAdBtn.setOnClickListener(this);


    }
    private void captureThirdImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MEDIA_TYPE_THIRD_IMAGE);
    }
    private void captureSecondImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MEDIA_TYPE_SECOND_IMAGE);
    }
    private void captureImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, MEDIA_TYPE_IMAGE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MEDIA_TYPE_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            firstimg.setImageBitmap(photo);

        }
        else if(requestCode == MEDIA_TYPE_SECOND_IMAGE && resultCode == RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            secondimg.setImageBitmap(photo);
        }
        else if(requestCode == MEDIA_TYPE_SECOND_IMAGE && resultCode == RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            thirdimg.setImageBitmap(photo);
        }
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
            switch (id)
            {
                case R.id.postMyAdd:
                    boolean cancelFlag = false;
                    View focusView = null;
                    String petTitleStr = petTitle.getText().toString().trim();
                    String petPriceStr = petPrice.getText().toString().trim();
                    petTitle.setError(null);
                    petPrice.setError(null);
                    if(TextUtils.isEmpty(petTitleStr))
                    {
                        focusView = petTitle;
                        petTitle.setError("Please Provide pet title");
                        cancelFlag = true;
                    }
                    else if(petTitleStr.toString().trim().length() < 3  )
                    {
                        focusView = petTitle;
                        petTitle.setError("pet name should have atleast 3 character");
                        cancelFlag = true;
                    }
                    else if(petTitleStr.toString().trim().length() > 30 )
                    {
                        focusView = petTitle;
                        petTitle.setError("pet name should not be grater than 30 character");
                        cancelFlag = true;
                    }
                    if(TextUtils.isEmpty(petPriceStr))
                    {
                        focusView = petPrice;
                        petPrice.setError("please enter pet price");
                        cancelFlag = true;
                    }
                    else if(petPriceStr.toString().trim().length() > 4)
                    {
                        focusView = petPrice;
                        petPrice.setError("pet prices should have 4 digit ");
                        cancelFlag = true;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"All validations are done",Toast.LENGTH_LONG).show();
                    }
            }

    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error, int requestTokan) {

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data, int requestTokan) {

    }
}
