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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.FileUploadCallback;
import com.app.myplant.callback.InstructionCallback;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.callback.PlantCategoryCallback;
import com.app.myplant.controllers.ImageController;
import com.app.myplant.controllers.InstructionController;
import com.app.myplant.controllers.PlantCategoryController;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.helper.ImagePicker;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Instruction;
import com.app.myplant.model.Plant;
import com.app.myplant.model.PlantCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddInstuctionActivity extends AppCompatActivity {

    EditText etxt_name , etxt_details;
    Spinner plant_category;
    ImageView plantImage;
    Instruction plant;
    TextView txt_save;


    private static final int RESULT_LOAD_IMAGES = 25;
    String imageURL = "";

    LoadingHelper loadingHelper;
    ArrayList<PlantCategory> plantCategories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instuction);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.plant_date);

        etxt_details = findViewById(R.id.etxt_details);
        etxt_name = findViewById(R.id.etxt_name);
        txt_save = findViewById(R.id.txt_save);
        plantImage = findViewById(R.id.plant_image);
        plant_category = findViewById(R.id.etxt_plant_category);

        loadingHelper = new LoadingHelper(this);
        if(SharedData.instruction == null){
            plant = new Instruction();
            plant.setKey("");
        }else{
            plant = SharedData.instruction;
            etxt_name.setText(plant.getTitle());
            etxt_details.setText(plant.getDescription());

            Picasso.get()
                    .load(plant.getImageURL())
                    .into(plantImage);

            imageURL = plant.getImageURL();
        }



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


                if(etxt_details.getText() == null){
                    etxt_details.setError(getString(R.string.required));
                    return;
                }else if(etxt_details.getText().toString().equals("")){
                    etxt_details.setError(getString(R.string.required));
                    return;
                }

                if(imageURL.equals("")){
                    Toast.makeText(AddInstuctionActivity.this, "Please,Select Plant Image First", Toast.LENGTH_SHORT).show();
                    return;
                }

                plant.setDescription(etxt_details.getText().toString());
                plant.setImageURL(imageURL);
                plant.setTitle(etxt_name.getText().toString());


                loadingHelper.showLoading("Saving Data");
                new InstructionController().Save(plant, new InstructionCallback() {
                    @Override
                    public void onSuccess(ArrayList<Instruction> chats) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(AddInstuctionActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }

                    @Override
                    public void onFail(String msg) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(AddInstuctionActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        plant_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                plant.setPlantCategory(plantCategories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new PlantCategoryController().getPlantCategorys(new PlantCategoryCallback() {
            @Override
            public void onSuccess(ArrayList<PlantCategory> chats) {
                plantCategories = chats;
                String[] data = new String[plantCategories.size()];
                for (int i = 0;i<chats.size();i++){
                    data[i] = plantCategories.get(i).getName();
                }

                ArrayAdapter aa = new ArrayAdapter(AddInstuctionActivity.this,android.R.layout.simple_spinner_item,data);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                plant_category.setAdapter(aa);
            }

            @Override
            public void onFail(String msg) {

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