package com.app.myplant.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.FileUploadCallback;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.callback.PlantCategoryCallback;
import com.app.myplant.controllers.ImageController;
import com.app.myplant.controllers.PlantCategoryController;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.helper.ImagePicker;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Plant;
import com.app.myplant.model.PlantCategory;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PlantDataActivity extends AppCompatActivity {

    EditText etxt_name ,etxt_category, etxt_details;
    ImageView plantImage;
    Plant plant;
    TextView txt_save;

    ArrayList<PlantCategory> plantCategories ;
    PlantCategory selectedCategory;
    ArrayAdapter<String> categoriesAdapter;
    List<String> categoriesNames;

    private static final int RESULT_LOAD_IMAGES = 25;
    String imageURL = "";

    LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_data);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.plant_date);

        etxt_category= findViewById(R.id.etxt_category);
        etxt_details = findViewById(R.id.etxt_details);
        etxt_name = findViewById(R.id.etxt_name);
        txt_save = findViewById(R.id.txt_save);
        plantImage = findViewById(R.id.plant_image);

        loadingHelper = new LoadingHelper(this);
        if(SharedData.plant == null){
            plant = new Plant();
            plant.setKey("");
        }else{
            plant = SharedData.plant;
            etxt_name.setText(plant.getName());
            etxt_details.setText(plant.getDetails());
            etxt_category.setText(plant.getCategory().getName());
            selectedCategory = plant.getCategory();

            Picasso.get()
                    .load(plant.getImageURL())
                    .into(plantImage);

            imageURL = plant.getImageURL();
        }



        loadCategories();

        etxt_category.setFocusable(false);
        etxt_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesAdapter = new ArrayAdapter<>(PlantDataActivity.this, android.R.layout.simple_list_item_1);
                categoriesAdapter.addAll(categoriesNames);

                AlertDialog.Builder dialog = new AlertDialog.Builder(PlantDataActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = (EditText) dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list);
                dialogTitle.setText(R.string.select_category);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(categoriesAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        categoriesAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        etxt_category.setText(categoriesAdapter.getItem(position));
                        for (PlantCategory category : plantCategories){
                            if(category.getName().equals(etxt_category.getText().toString())){
                                selectedCategory = category;
                                break;
                            }
                        }
                    }
                });
            }
        });

        plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkReadPermission()){
                    pickDoc();
                }
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etxt_name.getText() == null){
                    etxt_name.setError(getString(R.string.required));
                    return;
                }else if(etxt_name.getText().toString().equals("")){
                    etxt_name.setError(getString(R.string.required));
                    return;
                }


                if(etxt_category.getText() == null){
                    etxt_category.setError(getString(R.string.required));
                    return;
                }else if(etxt_category.getText().toString().equals("")){
                    etxt_category.setError(getString(R.string.required));
                    return;
                }

                if(etxt_details.getText() == null){
                    etxt_details.setError(getString(R.string.required));
                    return;
                }else if(etxt_details.getText().toString().equals("")){
                    etxt_details.setError(getString(R.string.required));
                    return;
                }

                if(imageURL.equals("")){
                    Toast.makeText(PlantDataActivity.this, "Please,Select Plant Image First", Toast.LENGTH_SHORT).show();
                    return;
                }

                plant.setCategory(selectedCategory);
                plant.setDetails(etxt_details.getText().toString());
                plant.setImageURL(imageURL);
                plant.setName(etxt_name.getText().toString());


                loadingHelper.showLoading("Saving Data");
                new PlantController().Save(plant, new PlantCallback() {
                    @Override
                    public void onSuccess(ArrayList<Plant> chats) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(PlantDataActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }

                    @Override
                    public void onFail(String msg) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(PlantDataActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkReadPermission(){
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},2);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickDoc();
            }
        }
    }

    private void loadCategories(){
        new PlantCategoryController()
                .getPlantCategorys(new PlantCategoryCallback() {
                    @Override
                    public void onSuccess(ArrayList<PlantCategory> chats) {
                        plantCategories = chats;
                        categoriesNames = new ArrayList<>();
                        for (PlantCategory plantCategory : plantCategories){
                            categoriesNames.add(plantCategory.getName());
                        }
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
    }

    private void pickDoc(){
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, RESULT_LOAD_IMAGES);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGES && resultCode == Activity.RESULT_OK) {
//            if (data == null) {
//                //Display an error
//                return;
//            }

            loadingHelper.showLoading("Uploading Image");
//            Bitmap bitmap =
//                    ImagePicker.getImageFromResult(this, resultCode, data);
            Uri uri = ImagePicker.getUriFromResult(this, resultCode, data);;
            if(uri == null){
                Toast.makeText(this, "Cannot load image", Toast.LENGTH_SHORT).show();
            }else{
                new ImageController().uploadImage(uri, new FileUploadCallback() {
                    @Override
                    public void onSuccess(String url) {
                        imageURL = url;
                        loadingHelper.dismissLoading();
                        Picasso.get()
                                .load(uri)
                                .into(plantImage);
                    }

                    @Override
                    public void onFail(String msg) {
                        loadingHelper.dismissLoading();
                    }
                });
            }




        }
    }


}