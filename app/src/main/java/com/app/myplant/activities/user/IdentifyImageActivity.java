package com.app.myplant.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.FileUploadCallback;
import com.app.myplant.controllers.ImageController;
import com.app.myplant.helper.ImagePicker;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.identify_image.FilePathHelper;
import com.app.myplant.identify_image.IdentifyPlant;
import com.app.myplant.identify_image.IdentifyPlantInterface;
import com.app.myplant.identify_image.ImageIdenifyResponse;
import com.app.myplant.identify_image.ImageUploadResponse;
import com.app.myplant.identify_image.UploadImageRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class IdentifyImageActivity extends AppCompatActivity {

    ImageView plantImage;
    TextView txt_save;
    private static final int RESULT_LOAD_IMAGES = 25;

    Bitmap bitmap;
    LoadingHelper loadingHelper;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_image);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.identify_image);

        recyclerView = findViewById(R.id.result_images);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadingHelper = new LoadingHelper(this);
        txt_save = findViewById(R.id.txt_save);
        plantImage = findViewById(R.id.plant_image);

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
                loadingHelper.showLoading("Uploading Image");
                IdentifyPlantInterface identifyPlantInterface = IdentifyPlant.getApiClient().create(IdentifyPlantInterface.class);
                String[] modifiers=  new String[1];
                modifiers[0] = "similar_images";
                UploadImageRequest uploadImageRequest = new UploadImageRequest();
                uploadImageRequest.modifiers = modifiers;
                String imageImBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                uploadImageRequest.images = new String[]{imageImBase64};
                Call<ImageUploadResponse> response =identifyPlantInterface.uploadImage(uploadImageRequest);
                response.enqueue(new retrofit2.Callback<ImageUploadResponse>() {
                    @Override
                    public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                        loadingHelper.dismissLoading();
                        if(response.isSuccessful()){
                            ImageUploadResponse imageUploadResponse = response.body();
                            String secret = imageUploadResponse.getSecret();

                            loadingHelper.showLoading("Identify Plant");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    Call<ImageIdenifyResponse> responseCall= identifyPlantInterface.identifyImage(secret);
                                    responseCall.enqueue(new retrofit2.Callback<ImageIdenifyResponse>() {
                                        @Override
                                        public void onResponse(Call<ImageIdenifyResponse> call, Response<ImageIdenifyResponse> response) {
                                            loadingHelper.dismissLoading();
                                            if(response.isSuccessful()){
                                                ImageIdenifyResponse imageIdenifyResponse = response.body();
                                                if(imageIdenifyResponse == null){
                                                    imageIdenifyResponse = new ImageIdenifyResponse();
                                                    imageIdenifyResponse.suggestions = new ArrayList<>();
                                                }else if(imageIdenifyResponse.suggestions == null){
                                                    imageIdenifyResponse.suggestions = new ArrayList<>();
                                                }
                                                if(imageIdenifyResponse.suggestions.isEmpty()){
                                                    Toast.makeText(IdentifyImageActivity.this, "No Result Found", Toast.LENGTH_SHORT).show();
                                                }

                                                recyclerView.setAdapter(new PlantResultAdapter(imageIdenifyResponse));

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ImageIdenifyResponse> call, Throwable t) {
                                            loadingHelper.dismissLoading();
                                        }
                                    });
                                }
                            }, 2000);




                        }
                    }

                    @Override
                    public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(IdentifyImageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
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


//            Bitmap bitmap =
//                    ImagePicker.getImageFromResult(this, resultCode, data);
            Uri uri = ImagePicker.getUriFromResult(this, resultCode, data);;
            if(uri == null){
                Toast.makeText(this, "Cannot load image", Toast.LENGTH_SHORT).show();
            }else{
                File file = new File(Objects.requireNonNull(FilePathHelper.getRealPathFromURI(
                        IdentifyImageActivity.this, uri)));
                bitmap= BitmapFactory.decodeFile(file.getPath());
                plantImage.setBackground(null);
                Picasso.get()
                        .load(file)
                        .into(plantImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(IdentifyImageActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(IdentifyImageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }




        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    class PlantResultAdapter extends RecyclerView.Adapter<PlantResultAdapter.ViewHolder> {

        ImageIdenifyResponse imageIdenifyResponse;

        public PlantResultAdapter(ImageIdenifyResponse imageIdenifyResponse) {
            this.imageIdenifyResponse = imageIdenifyResponse;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(IdentifyImageActivity.this)
                    .inflate(R.layout.result_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ImageIdenifyResponse.Suggestion suggestion = imageIdenifyResponse.getSuggestions().get(position);

            Picasso.get()
                    .load(suggestion.plantDetails.wikiImage.value)
                    .into(holder.plantImage);

            double propability = Double.parseDouble(suggestion.probability) * 100;
            holder.percentage.setText(String.format("%s %%", String.format(Locale.ENGLISH, "%.2f", propability)));
            holder.description.setText(suggestion.plantDetails.wikiDescription.value);
            holder.name.setText(suggestion.plant_name);
        }

        @Override
        public int getItemCount() {
            return imageIdenifyResponse.suggestions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView plantImage;
            TextView name,percentage,description;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                plantImage = itemView.findViewById(R.id.plant_image);
                name = itemView.findViewById(R.id.name);
                percentage = itemView.findViewById(R.id.percentage);
                description = itemView.findViewById(R.id.description);
            }
        }
    }


}