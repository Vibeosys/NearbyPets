package com.nearbypets.activities;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nearbypets.R;
import com.nearbypets.utils.DecimalDigitsInputFilter;
import com.nearbypets.utils.ServerSyncManager;

import org.json.JSONObject;

public class PostMyAdActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnStringErrorReceived,ServerSyncManager.OnStringResultReceived {

    private ArrayAdapter<String> mCategoryAdapter;
    private ArrayAdapter<String> mTypeAdapter;
    private Spinner spnCategory;
    private Spinner spnType;
    private Button postMyAdBtn;
    private EditText petTitle,petDecsription,petPrice;

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

        postMyAdBtn.setOnClickListener(this);

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
