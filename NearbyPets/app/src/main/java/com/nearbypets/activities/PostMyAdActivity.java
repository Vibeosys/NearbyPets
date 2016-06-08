package com.nearbypets.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nearbypets.MainActivity;
import com.nearbypets.R;
import com.nearbypets.adapters.PostAdSpinnerAdapter;
import com.nearbypets.adapters.PostedAdBirdCategoryAdapter;
import com.nearbypets.data.BirdCategoryDataDTO;
import com.nearbypets.data.ImagesDbDTO;
import com.nearbypets.data.PostMyAdDBDTO;
import com.nearbypets.data.TableDataDTO;
import com.nearbypets.data.TypeDataDTO;
import com.nearbypets.data.downloaddto.DownloadBirdCategoryDataDTO;
import com.nearbypets.data.downloaddto.DownloadTypeDataDTO;
import com.nearbypets.service.GPSTracker;
import com.nearbypets.utils.ConstantOperations;
import com.nearbypets.utils.NetworkUtils;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostMyAdActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private ArrayAdapter<String> mCategoryAdapter;
    private ArrayAdapter<String> mTypeAdapter;
    private Spinner spnCategory;
    private Spinner spnType;
    private Spinner addSpinner;
    private Button postMyAdBtn;
    private RadioGroup radioGroup;
    private RadioButton radioFullAddress, radioCity, checkBtn;
    private EditText petTitle, petDecsription, petPrice;
    private ImageView firstimg, secondimg, thirdimg;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_SECOND_IMAGE = 2;
    public static final int MEDIA_TYPE_THIRD_IMAGE = 3;
    private final int REQ_TOKEN_POST_ADD_CATEGORY = 15;
    private final int REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER = 16;
    ArrayList<ImagesDbDTO> images = new ArrayList<>();
    private Bitmap bitmap;
    String mlongi;
    String mlat;
    ArrayList<TypeDataDTO> categoryDatas = new ArrayList<>();
    PostAdSpinnerAdapter spAdapt;
    PostedAdBirdCategoryAdapter firstAdapt;
    private View formView;
    private View progressBar;
    private final int REQ_TOKEN_POSTMYAD = 20;
    GPSTracker gpsTracker;
    List<String> gpsAddressList = new ArrayList<String>();
    String userId, display_city, display_full_address = null;
    int bird_categoryId, bird_Type;
    String Full_Address_To_send, City_TO_send;
    boolean setFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my_add);
        setTitle(getResources().getString(R.string.activity_post_ad));
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnType = (Spinner) findViewById(R.id.spnType);
        postMyAdBtn = (Button) findViewById(R.id.postMyAdd);
        petTitle = (EditText) findViewById(R.id.pet_title);
        petDecsription = (EditText) findViewById(R.id.pet_description);
        petPrice = (EditText) findViewById(R.id.pet_price);
        firstimg = (ImageView) findViewById(R.id.firstImg);
        secondimg = (ImageView) findViewById(R.id.secondImg);
        thirdimg = (ImageView) findViewById(R.id.thirdImg);
        addSpinner = (Spinner) findViewById(R.id.address_spinner);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioFullAddress = (RadioButton) findViewById(R.id.fullAddress);
        radioCity = (RadioButton) findViewById(R.id.cityName);
        radioFullAddress.setChecked(true);
        formView = findViewById(R.id.formViewPostAd);
        progressBar = findViewById(R.id.progressBar);
        gpsTracker = new GPSTracker(getApplicationContext());
        if (gpsTracker.getIsGPSTrackingEnabled()) {


            Full_Address_To_send = gpsTracker.getAddressLine(getApplicationContext()) + " " + gpsTracker.getLocality(getApplicationContext());
            City_TO_send = gpsTracker.getLocality(getApplicationContext());
            gpsAddressList.add(gpsTracker.getAddressLine(getApplicationContext()) + " " + gpsTracker.getLocality(getApplicationContext()));

            mlat = String.valueOf(gpsTracker.getLatitude());
            mlongi = String.valueOf(gpsTracker.getLongitude());
            //  Toast.makeText(getApplicationContext(), "Testing  "+mlat.toString(), Toast.LENGTH_LONG).show();
        } else {
            createAlertDialog("GPS ", "GPS location is disable");
        }

       /* Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(18.5487939, 73.7894986, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String featureName = addresses.get(0).getFeatureName();

         //   Toast.makeText(getApplicationContext(),""+address+city+state+featureName,Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        userId = mSessionManager.getUserId();

        display_city = gpsTracker.getLocality(getApplicationContext());
        display_full_address = gpsTracker.getLocality(getApplicationContext()) + " " + gpsTracker.getCountryName(getApplicationContext());
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {

            createAlertNetWorkDialog("Network Error", "Please check newtwork connection");


        }

        getPostAddCategory();
        getPostAdCategoryFirstSpineer();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);


        spAdapt = new PostAdSpinnerAdapter(getApplicationContext());
        firstAdapt = new PostedAdBirdCategoryAdapter(getApplicationContext());

        spnCategory.setAdapter(firstAdapt);
        spnType.setAdapter(spAdapt);


        if (gpsAddressList.size() >= 1) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gpsAddressList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            addSpinner.setAdapter(dataAdapter);
        }


        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BirdCategoryDataDTO mCategories = (BirdCategoryDataDTO) firstAdapt.getItem(position);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MEDIA_TYPE_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri filePath = getImageUri(getApplicationContext(), photo);
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                // imageView.setImageBitmap(bitmap);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String currentDateandTime = sdf.format(new Date());
                String test = currentDateandTime + ".JPG";

                String strIMG = getStringImage(bitmap);
                ImagesDbDTO images1 = new ImagesDbDTO(1, test, strIMG);
                images.add(images1);
                firstimg.setImageBitmap(photo);
                setFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == MEDIA_TYPE_SECOND_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri filePath1 = getImageUri(getApplicationContext(), photo);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String currentDateandTime = sdf.format(new Date());
                String test = currentDateandTime + ".JPG";

                String strIMG = getStringImage(bitmap);
                ImagesDbDTO images2 = new ImagesDbDTO(2, test, strIMG);
                images.add(images2);
                secondimg.setImageBitmap(photo);
                setFlag = true;

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == MEDIA_TYPE_THIRD_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri filePath2 = getImageUri(getApplicationContext(), photo);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String currentDateandTime = sdf.format(new Date());
                String test = currentDateandTime + ".JPG";

                String strIMG = getStringImage(bitmap);
                ImagesDbDTO images3 = new ImagesDbDTO(3, test, strIMG);
                images.add(images3);
                thirdimg.setImageBitmap(photo);
                setFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                } else if (petTitleStr.toString().trim().length() < 3) {
                    focusView = petTitle;
                    petTitle.requestFocus();
                    petTitle.setError("pet name should have atleast 3 character");
                    cancelFlag = true;
                } else if (petTitleStr.toString().trim().length() > 30) {
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
                } else if (petPriceStr.toString().trim().length() > 4) {
                    focusView = petPrice;
                    petPrice.requestFocus();
                    petPrice.setError("pet prices should have 4 digit ");
                    cancelFlag = true;
                } else if (setFlag == false) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Please upload atleast one image", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    cancelFlag = true;
                }
                /*if(cancelFlag)
                {
                    focusView.requestFocus();
                }*/
                else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    checkBtn = (RadioButton) findViewById(selectedId);
                    if (checkBtn.getText().equals("Display Full Address")) {
                        // Toast.makeText(getApplicationContext(), "Display full address is clicked", Toast.LENGTH_LONG).show();
                        callToUploadMyAd(Full_Address_To_send);
                    } else if (checkBtn.getText().equals("Display City Only")) {
                        callToUploadMyAd(City_TO_send);
                        // Toast.makeText(getApplicationContext(), "Display city is clicked", Toast.LENGTH_LONG).show();
                    }
                    // callToUploadMyAd();
                   /* createAlertDialog("Post Ad", "Ad Posted sucess fully");

                    Toast.makeText(getApplicationContext(), "All validations are done", Toast.LENGTH_LONG).show();*/
                }
        }

    }
    // public PostMyAdDBDTO(int categoryId, String title, String description, String address,
    // String displayAddress, String latitude, String longitude, double price, int typeId, String userId, ArrayList<ImagesDbDTO> images)


    public void callToUploadMyAd(String displayAddress) {
        showProgress(true, formView, progressBar);
        PostMyAdDBDTO postMyAdDBDTO = new PostMyAdDBDTO(bird_categoryId, petTitle.getText().toString(), "" + petDecsription.getText().toString(), display_city, displayAddress, "" + gpsTracker.getLatitude(), "" + gpsTracker.getLongitude(), Double.parseDouble(petPrice.getText().toString()), bird_Type, userId, images);
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
    public void onResultReceived(@NonNull JSONObject data, int requestToken) {
        switch (requestToken) {
            case REQ_TOKEN_POST_ADD_CATEGORY:
                showProgress(false, formView, progressBar);
                Log.d("Succes", "##REQ" + data.toString());
                try {
                    DownloadTypeDataDTO downloadTypeDataDTO = new Gson().fromJson(data.toString(), DownloadTypeDataDTO.class);
                    updateSettings(downloadTypeDataDTO.getSettings());
                    getAdType(downloadTypeDataDTO.getData().get(0).getData());
                    /*categoryDatas = downloadTypeDataDTO.getData();
                    spAdapt.notifyDataSetChanged();*/

                } catch (JsonSyntaxException e) {

                }
                break;
            case REQ_TOKEN_POST_ADD_CATEGORY_FIRST_SPINEER:
                showProgress(false, formView, progressBar);
                Log.d("Succes", "##REQ" + data.toString());
                try {
                    DownloadBirdCategoryDataDTO downloadBirdCategoryDataDTO = new Gson().fromJson(data.toString(), DownloadBirdCategoryDataDTO.class);
                    updateSettings(downloadBirdCategoryDataDTO.getSettings());
                    getAdFirstSpineer(downloadBirdCategoryDataDTO.getData().get(0).getData());
                    /*categoryDatas = downloadTypeDataDTO.getData();
                    spAdapt.notifyDataSetChanged();*/

                } catch (JsonSyntaxException e) {

                }
                break;
            case REQ_TOKEN_POSTMYAD:
                showProgress(false, formView, progressBar);
                // createAlertDialog("Post Ad", "Ad Posted success fully");
                Toast toast = Toast.makeText(getApplicationContext(), "Ad Posted successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                Log.d("Successs for post ad", "##REQ" + data.toString());

        }

    }

    private void getAdFirstSpineer(ArrayList<BirdCategoryDataDTO> data) {
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
        try {
            System.gc();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);


        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.d("TAG", "## ");
        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
