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
    private Spinner spnType;
    private Spinner spnCategory;
    private Button removeImage1, removeImage2, removeImage3;
    private ImagesDbDTO imagesDbDTO;
    private int addresInt=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_add);
        setTitle(getResources().getString(R.string.activity_post_ad));
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnType = (Spinner) findViewById(R.id.spnType);
        Button postMyAdBtn = (Button) findViewById(R.id.postMyAdd);
        petTitle = (EditText) findViewById(R.id.pet_title);
        petDecsription = (EditText) findViewById(R.id.pet_description);
        petPrice = (EditText) findViewById(R.id.pet_price);
        firstimg = (ImageView) findViewById(R.id.firstImg);
        secondimg = (ImageView) findViewById(R.id.secondImg);
        thirdimg = (ImageView) findViewById(R.id.thirdImg);
        addSpinner = (TextView) findViewById(R.id.address_spinner);
        removeImage1 = (Button) findViewById(R.id.removeImage1);
        removeImage2 = (Button) findViewById(R.id.removeImage2);
        removeImage3 = (Button) findViewById(R.id.removeImage3);
        addressSelectionRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioFullAddress = (RadioButton) findViewById(R.id.fullAddress);
        //radioCity = (RadioButton) findViewById(R.id.cityName);
        radioFullAddress.setChecked(true);
        formView = findViewById(R.id.formViewPostAd);
        progressBar = findViewById(R.id.progressBar);

        removeImage1.setVisibility(View.INVISIBLE);
        removeImage2.setVisibility(View.INVISIBLE);
        removeImage3.setVisibility(View.INVISIBLE);


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
        } else {
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
                    captureFirstImage();
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

            removeImage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstimg.setImageResource(R.drawable.default_pet_image);
                    removeImage1.setVisibility(View.INVISIBLE);

                    removeImage(1);

                }
            });
            removeImage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    secondimg.setImageResource(R.drawable.default_pet_image);
                    removeImage2.setVisibility(View.INVISIBLE);
                    removeImage(2);
                }
            });
            removeImage3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thirdimg.setImageResource(R.drawable.default_pet_image);
                    removeImage3.setVisibility(View.INVISIBLE);
                    removeImage(3);
                }
            });
        }


    }

    private void removeImage(int id) {
        for (int i = 0; i < images.size(); i++) {
            ImagesDbDTO imagesDbDTO = images.get(i);
            if (imagesDbDTO.getImageId() == id) {
                images.remove(i);
                return;
            }
        }

    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectedId = addressSelectionRadioGroup.getCheckedRadioButtonId();
        RadioButton checkBtn = (RadioButton) findViewById(selectedId);

        if (checkBtn.getText().equals("Display City Only")) {
            addSpinner.setText(cityToDisplay);
            addresInt=1;
        } else {
            addSpinner.setText(completeAddress);
            addresInt=2;
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
        getPermissionsForReadWriteStorage(PERMISSION_REQUEST_MEDIA_TYPE_THIRD_IMAGE);
    }

    private void captureSecondImage() {
        getPermissionsForReadWriteStorage(PERMISSION_REQUEST_MEDIA_TYPE_SECOND_IMAGE);
    }

    private void captureFirstImage() {
        getPermissionsForReadWriteStorage(PERMISSION_REQUEST_MEDIA_TYPE_IMAGE);
    }

    @Override
    protected void performReadWriteStorageAction(int requestCode) {
        Intent theIntent = new Intent(PostMyAdActivity.this, GridViewPhotos.class);
        startActivityForResult(theIntent, requestCode);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }
        int imageNumber = 0;
        int scaledHeight = 480;
        int scaledWidth = 320;
        String imagePath = data.getExtras().getString("imagePath");
        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap convertedImg = null;
        try {
            options.inJustDecodeBounds = true;
            convertedImg = BitmapFactory.decodeFile(imagePath, options);
            options.inSampleSize = calculateInSampleSize(options, 480, 320);
            options.inJustDecodeBounds = false;
            convertedImg = BitmapFactory.decodeFile(imagePath, options);
            System.gc();
        } catch (Exception e) {
            createAlertDialog("Post My Ad", "Image cannot be uploaded");
            Log.d("TAG", "##" + e.toString());
            System.gc();
            return;
        }


        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(convertedImg, scaledHeight, scaledWidth, true);
            System.gc();
        } catch (Exception e) {
            createAlertDialog("Post My Ad", "Image cannot be uploaded");
            Log.d("TAG", "##" + e.toString());
            System.gc();
            return;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        String imageWithExtension = currentDateandTime + ".JPG";

        String imageInBase64Format = getStringImage(scaledBitmap);
        if (scaledBitmap != convertedImg)
            convertedImg.recycle();
        convertedImg = null;
        System.gc();

        if (requestCode == PERMISSION_REQUEST_MEDIA_TYPE_IMAGE) {
            imageNumber = 1;

            firstimg.setImageBitmap(scaledBitmap);
            removeImage1.setVisibility(View.VISIBLE);
        }
        if (requestCode == PERMISSION_REQUEST_MEDIA_TYPE_SECOND_IMAGE) {
            imageNumber = 2;
            secondimg.setImageBitmap(scaledBitmap);
            removeImage2.setVisibility(View.VISIBLE);

        }
        if (requestCode == PERMISSION_REQUEST_MEDIA_TYPE_THIRD_IMAGE) {
            imageNumber = 3;
            thirdimg.setImageBitmap(scaledBitmap);
            removeImage3.setVisibility(View.VISIBLE);
        }


        imagesDbDTO = new ImagesDbDTO(imageNumber, imageWithExtension, imageInBase64Format);
        images.add(imagesDbDTO);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
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
                //boolean cancelFlag = false;
                //View focusView = null;
                String petTitleStr = petTitle.getText().toString().trim();
                String petPriceStr = petPrice.getText().toString().trim();
                petTitle.setError(null);
                petPrice.setError(null);
                if (TextUtils.isEmpty(petTitleStr)) {
                    //focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("Please Provide pet title");
                    //cancelFlag = true;
                } else if (petTitleStr.length() < 3) {
                    //focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("pet name should have atleast 3 character");
                    //cancelFlag = true;
                } else if (petTitleStr.length() > 30) {
                    //focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("pet name should not be grater than 30 character");
                    //cancelFlag = true;
                }
                if (TextUtils.isEmpty(petPriceStr)) {
                    //focusView = petPrice;
                    petPrice.requestFocus();
                    petPrice.setError("please enter pet price");
                    //cancelFlag = true;
                } /*else if (petPriceStr.length() > 4) {
                    //focusView = petPrice;
                    petPrice.requestFocus();
                    petPrice.setError("pet prices should have 4 digit ");
                    //cancelFlag = true;
                }*/ else {
                    if (spnCategory.getSelectedItemPosition() == 0) {
                        createAlertDialog("Post My Ad", "Please Select Category");
                        return;
                    } else
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
                images,addresInt);
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
                if (!typeDataDTOs.isEmpty() && typeDataDTOs.size() > 2)
                    spnType.setSelection(1);
                break;
            case REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER:
                showProgress(false, formView, progressBar);
                Log.d("Succes", "##REQ" + data.toString());
                ArrayList<CategoryDataDTO> birdCategoryDataDTOs = CategoryDataDTO.deserializeToArray(data);
                birdCategoryDataDTOs.add(0, new CategoryDataDTO(0, "None"));
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
