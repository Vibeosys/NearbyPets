package com.nearbypets.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nearbypets.MainActivity;
import com.nearbypets.R;
import com.nearbypets.adapters.PostAdSpinnerAdapter;
import com.nearbypets.adapters.PostedAdBirdCategoryAdapter;
import com.nearbypets.data.CategoryDataDTO;
import com.nearbypets.data.ImagesDbDTO;
import com.nearbypets.data.PostMyAdDBDTO;
import com.nearbypets.data.SettingsDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.TypeDataDTO;
import com.nearbypets.data.downloaddto.ErrorDbDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostMyAdActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived,
        RadioGroup.OnCheckedChangeListener {

    private TextView addSpinner;
    private RadioGroup addressSelectionRadioGroup;
    private EditText petTitle, petDecsription, petPrice;
    private ImageView firstimg, secondimg, thirdimg;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_SECOND_IMAGE = 2;
    public static final int MEDIA_TYPE_THIRD_IMAGE = 3;
    private final int REQ_TOKEN_POST_ADD_CATEGORY = 15;
    private final int REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER = 16;
    private final int REQ_TOKEN_POSTMYAD = 20;
    private ArrayList<ImagesDbDTO> images = new ArrayList<>();
    private PostAdSpinnerAdapter spAdapt;
    private PostedAdBirdCategoryAdapter firstAdapt;
    private View formView;
    private View progressBar;
    private String userId, display_full_address = null;
    private int bird_categoryId, bird_Type;
    private String completeAddress, cityToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_add);
        setTitle(getResources().getString(R.string.activity_post_ad));
        Spinner spnCategory = (Spinner) findViewById(R.id.spnCategory);
        Spinner spnType = (Spinner) findViewById(R.id.spnType);
        Button postMyAdBtn = (Button) findViewById(R.id.postMyAdd);
        petTitle = (EditText) findViewById(R.id.pet_title);
        petDecsription = (EditText) findViewById(R.id.pet_description);
        petPrice = (EditText) findViewById(R.id.pet_price);
        firstimg = (ImageView) findViewById(R.id.firstImg);
        secondimg = (ImageView) findViewById(R.id.secondImg);
        thirdimg = (ImageView) findViewById(R.id.thirdImg);
        addSpinner = (TextView) findViewById(R.id.address_spinner);
        addressSelectionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioFullAddress = (RadioButton) findViewById(R.id.fullAddress);
        //radioCity = (RadioButton) findViewById(R.id.cityName);
        radioFullAddress.setChecked(true);
        formView = findViewById(R.id.formViewPostAd);
        progressBar = findViewById(R.id.progressBar);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation(locationManager);
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext(), currentLat, currentLong);

        completeAddress = gpsTracker.getCompleteAddress(getApplicationContext());
        cityToDisplay = gpsTracker.getLocality(getApplicationContext());
        addSpinner.setText(completeAddress);
        //gpsAddressList.add(completeAddress);

        userId = mSessionManager.getUserId();

        //display_city = cityToDisplay;
        display_full_address = completeAddress;
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            createAlertNetWorkDialog("Network Error", "Please check network connection");
        }

        getPostAddCategory();
        getPostAdCategoryFirstSpineer();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);


        spAdapt = new PostAdSpinnerAdapter(getApplicationContext());
        firstAdapt = new PostedAdBirdCategoryAdapter(getApplicationContext());

        spnCategory.setAdapter(firstAdapt);
        spnType.setAdapter(spAdapt);


        addressSelectionRadioGroup.setOnCheckedChangeListener(this);


        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryDataDTO mCategories = (CategoryDataDTO) firstAdapt.getItem(position);
                bird_categoryId = mCategories.getCategoryId();
                mCategories.getCategoryTitle();
                Log.d("TAG", "## " + mCategories.getCategoryId());
                Log.d("TAG", "## " + mCategories.getCategoryTitle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TypeDataDTO typeDataDTO = (TypeDataDTO) spAdapt.getItem(position);
                bird_Type = typeDataDTO.getTypeId();
                typeDataDTO.getTypeTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        firstimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        secondimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedId = addressSelectionRadioGroup.getCheckedRadioButtonId();
        RadioButton checkBtn = (RadioButton) findViewById(selectedId);

        if (checkBtn.getText().equals("Display City Only")) {
            addSpinner.setText(cityToDisplay);
        } else {
            addSpinner.setText(completeAddress);
        }
    }

    private void getPostAdCategoryFirstSpineer() {
        showProgress(true, formView, progressBar);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CATEGORY_ADPOST);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER, tableDataDTO);

    }

    private void getPostAddCategory() {
        showProgress(true, formView, progressBar);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CATEGORY_TYPES);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_POST_ADD_CATEGORY, tableDataDTO);
    }

    private void captureThirdImage() {

        startGridViewActivity(MEDIA_TYPE_THIRD_IMAGE);
    }

    private void captureSecondImage() {

        startGridViewActivity(MEDIA_TYPE_SECOND_IMAGE);
    }

    private void captureImage() {
        startGridViewActivity(MEDIA_TYPE_IMAGE);

    }

    private void startGridViewActivity(int requestCode) {
        Intent theIntent = new Intent(PostMyAdActivity.this, GridViewPhotos.class);
        startActivityForResult(theIntent, requestCode);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNumber = 0;
        String imagePath = data.getExtras().getString("imagePath");
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap convertedImg = BitmapFactory.decodeFile(imagePath, options);
        int scaledHeight = 50;
        int scaledWidth = 50;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(convertedImg, scaledHeight, scaledWidth, true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        String imageWithExtension = currentDateandTime + ".JPG";

        String imageInBase64Format = getStringImage(convertedImg);
        if (scaledBitmap != convertedImg)
            convertedImg.recycle();
        convertedImg = null;

        if (requestCode == MEDIA_TYPE_IMAGE) {
            imageNumber = 1;

            firstimg.setImageBitmap(scaledBitmap);
        }
        if (requestCode == MEDIA_TYPE_SECOND_IMAGE) {
            imageNumber = 2;
            secondimg.setImageBitmap(scaledBitmap);
        }
        if (requestCode == MEDIA_TYPE_THIRD_IMAGE) {
            imageNumber = 3;
            thirdimg.setImageBitmap(scaledBitmap);
        }


        ImagesDbDTO imagesDbDTO = new ImagesDbDTO(imageNumber, imageWithExtension, imageInBase64Format);
        images.add(imagesDbDTO);
    }

    private Bitmap convertPathToBitmap(String imagePath) {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Bitmap resultBitmapImage = null;

        try {
            System.gc();
            fileInputStream = new FileInputStream(imagePath);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            resultBitmapImage = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (FileNotFoundException e) {
            Log.e("FileNotfound", e.toString());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.e("BitmapConversion", e.toString());
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    Log.e("BitmapConversion", e.toString());
                }
            }
        }
        return resultBitmapImage;
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
        switch (id) {
            case R.id.postMyAdd:
                boolean cancelFlag = false;
                View focusView = null;
                String petTitleStr = petTitle.getText().toString().trim();
                String petPriceStr = petPrice.getText().toString().trim();
                petTitle.setError(null);
                petPrice.setError(null);
                if (TextUtils.isEmpty(petTitleStr)) {
                    focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("Please Provide pet title");
                    cancelFlag = true;
                } else if (petTitleStr.length() < 3) {
                    focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("pet name should have atleast 3 character");
                    cancelFlag = true;
                } else if (petTitleStr.length() > 30) {
                    focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("pet name should not be grater than 30 character");
                    cancelFlag = true;
                }
                if (TextUtils.isEmpty(petPriceStr)) {
                    focusView = petPrice;
                    petPrice.requestFocus();
                    petPrice.setError("please enter pet price");
                    cancelFlag = true;
                } else if (petPriceStr.length() > 4) {
                    focusView = petPrice;
                    petPrice.requestFocus();
                    petPrice.setError("pet prices should have 4 digit ");
                    cancelFlag = true;
                } else {
                    callToUploadMyAd(display_full_address, addSpinner.getText().toString());

                }
        }

    }


    public void callToUploadMyAd(String originalAddress, String addressToDisplay) {
        showProgress(true, formView, progressBar);
        PostMyAdDBDTO postMyAdDBDTO = new PostMyAdDBDTO(
                bird_categoryId,
                petTitle.getText().toString(), "" + petDecsription.getText().toString(),
                originalAddress,
                addressToDisplay,
                "" + currentLat,
                "" + currentLong,
                Double.parseDouble(petPrice.getText().toString()),
                bird_Type,
                userId,
                images);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(postMyAdDBDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.POST_MY_AD, serializedJsonString);
        mServerSyncManager.uploadDataToServer(REQ_TOKEN_POSTMYAD, tableDataDTO);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_POST_ADD_CATEGORY:
                Log.d("Error", "##REQ" + error.toString());
                break;
            case REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER:
                Log.d("Error for first spineer", "##REQ" + error.toString());
                break;
            case REQ_TOKEN_POSTMYAD:
                showProgress(false, formView, progressBar);
                createAlertDialog("Post Ad", "Error in posting ad");
                Log.d("Error for post my ad", "##REQ" + error.toString());
                break;
        }

    }

    @Override
    public void onDataErrorReceived(ErrorDbDTO errorDbDTO, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_POSTMYAD:
                showProgress(false, formView, progressBar);
                createAlertDialog("Post Ad", "Error in posting ad");
                Log.d("Error for post my ad", "##REQ" + errorDbDTO.toString());
                break;
        }

    }

    @Override
    public void onResultReceived(@NonNull String data, @NonNull List<SettingsDTO> settings, int requestToken) {
        updateSettings(settings);
        switch (requestToken) {
            case REQ_TOKEN_POST_ADD_CATEGORY:
                showProgress(false, formView, progressBar);
                Log.d("Succes", "##REQ" + data.toString());
                ArrayList<TypeDataDTO> typeDataDTOs = TypeDataDTO.deserializeToArray(data);
                getAdType(typeDataDTOs);
                break;
            case REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER:
                showProgress(false, formView, progressBar);
                Log.d("Succes", "##REQ" + data.toString());
                ArrayList<CategoryDataDTO> birdCategoryDataDTOs = CategoryDataDTO.deserializeToArray(data);
                getAdFirstSpineer(birdCategoryDataDTOs);
                break;
            case REQ_TOKEN_POSTMYAD:
                showProgress(false, formView, progressBar);
                Toast toast = Toast.makeText(getApplicationContext(), "Ad Posted successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                //     Log.d("Successs for post ad", "##REQ" + data.toString());

        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {


    }

    private void getAdFirstSpineer(ArrayList<CategoryDataDTO> data) {
        firstAdapt.clear();
        for (int i = 0; i < data.size(); i++) {
            firstAdapt.addItem(data.get(i));
        }
        firstAdapt.notifyDataSetChanged();
    }


    private void getAdType(ArrayList<TypeDataDTO> data) {
        spAdapt.clear();

        for (int i = 0; i < data.size(); i++) {
            spAdapt.addItem(data.get(i));
        }
        spAdapt.notifyDataSetChanged();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = null;
        try {
            System.gc();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.d("TAG", "## ");
        }

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;*/
    }

}
