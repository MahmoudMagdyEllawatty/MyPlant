package com.app.myplant.activities.farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.identify_image.ImageIdenifyResponse;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Plant;

import java.util.ArrayList;

public class PlantDetailsActivity extends AppCompatActivity {

    TextView plant_type,sun_exposure,soil_type,bloom_time,color,native_area,toxicity,water;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(SharedData.infoPlant.getName());


        plant_type = findViewById(R.id.plant_type);
        sun_exposure = findViewById(R.id.sun_exposure);
        soil_type = findViewById(R.id.soil_type);
        bloom_time = findViewById(R.id.bloom_time);
        color = findViewById(R.id.color);
        native_area = findViewById(R.id.native_area);
        toxicity = findViewById(R.id.toxicity);
        water = findViewById(R.id.water);



        water.setText(SharedData.infoPlant.getWater());
        plant_type.setText(SharedData.infoPlant.getCategory().getName());
        sun_exposure.setText(SharedData.infoPlant.getSunExposure());
        soil_type.setText(SharedData.infoPlant.getSoilType());
        bloom_time.setText(SharedData.infoPlant.getBloomTime());
        color.setText(SharedData.infoPlant.getColor());
        native_area.setText(SharedData.infoPlant.getNativeArea());
        toxicity.setText(SharedData.infoPlant.getToxicity());

        if(SharedData.loggedUser != null){
            findViewById(R.id.sell).setVisibility(View.GONE);
        }else{
            if(SharedData.isSell){
                findViewById(R.id.sell).setVisibility(View.GONE);
            }
        }

        findViewById(R.id.sell)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSellDialog(SharedData.infoPlant);
                    }
                });

    }

    private void showSellDialog(Plant plant){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PlantDetailsActivity.this);

        View dialogView = LayoutInflater.from(PlantDetailsActivity.this)
                .inflate(R.layout.dialog_sell,null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        final Button dialogBtnSubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageButton dialogBtnClose = dialogView.findViewById(R.id.btn_close);
        final EditText etxt_price = dialogView.findViewById(R.id.etxt_price);
        final EditText etxt_qnt = dialogView.findViewById(R.id.etxt_qnt);
        final TextView title = dialogView.findViewById(R.id.title);


        title.setText(new StringBuilder().append("Sell ").append(plant.getName()).toString());

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialogBtnSubmit.setOnClickListener(v -> {

            if(etxt_price.getText() == null){
                etxt_price.setError(getString(R.string.required));
                return;
            }else if(etxt_price.getText().toString().equals("")){
                etxt_price.setError(getString(R.string.required));
                return;
            }


            if(etxt_qnt.getText() == null){
                etxt_qnt.setError(getString(R.string.required));
                return;
            }else if(etxt_qnt.getText().toString().equals("")){
                etxt_qnt.setError(getString(R.string.required));
                return;
            }



            FarmerPlant farmerPlant = new FarmerPlant();
            farmerPlant.setFarmer(SharedData.loggedFarmer);
            farmerPlant.setKey("");
            farmerPlant.setPlant(plant);
            farmerPlant.setPrice(Double.parseDouble(etxt_price.getText().toString()));
            farmerPlant.setQnt(Double.parseDouble(etxt_qnt.getText().toString()));


            new FarmerPlantController()
                    .Save(farmerPlant, new FarmerPlantCallback() {
                        @Override
                        public void onSuccess(ArrayList<FarmerPlant> chats) {
                            Toast.makeText(PlantDetailsActivity.this, "Plant Added to your shop successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

            alertDialog.dismiss();


        });


        dialogBtnClose.setOnClickListener(v -> alertDialog.dismiss());
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
}