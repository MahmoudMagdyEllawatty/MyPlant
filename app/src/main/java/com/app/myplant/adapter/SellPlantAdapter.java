package com.app.myplant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.activities.farmer.PlantDetailsActivity;
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SellPlantAdapter extends RecyclerView.Adapter<SellPlantAdapter.ViewHolder> {

    private ArrayList<Plant> Plants;
    private Context context;
    private ArrayList<FarmerPlant> farmerPlants;

    public SellPlantAdapter(ArrayList<Plant> Plants, Context context, ArrayList<FarmerPlant> farmerPlants) {
        this.Plants = Plants;
        this.context = context;
        this.farmerPlants = farmerPlants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.plant_sell_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant Plant = Plants.get(position);

        holder.title.setText(Plant.getName());
        holder.category.setText(Plant.getCategory().getName());
        holder.description.setText(Plant.getSunExposure());

        Picasso.get()
                .load(Plant.getImageURL())
                .into(holder.image);


        boolean isSell = isSell(Plant);
        if(isSell){
            holder.sell.setVisibility(View.GONE);
        }else{
            holder.sell.setVisibility(View.VISIBLE);
        }

        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSellDialog(Plant);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.isSell = isSell;
                SharedData.infoPlant = Plant;
                Intent intent = new Intent(context, PlantDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.isSell = isSell;
                SharedData.infoPlant = Plant;
                Intent intent = new Intent(context, PlantDetailsActivity.class);
                context.startActivity(intent);
            }
        });


    }

    private boolean isSell(Plant plant ){
        boolean state = false;
        for (FarmerPlant farmerPlant : farmerPlants){
            if(farmerPlant.getPlant().getKey().equals(plant.getKey())){
                state = true;
                break;
            }
        }

        return state;
    }

    private void showSellDialog(Plant plant){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context)
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
                etxt_price.setError(context.getString(R.string.required));
                return;
            }else if(etxt_price.getText().toString().equals("")){
                etxt_price.setError(context.getString(R.string.required));
                return;
            }


            if(etxt_qnt.getText() == null){
                etxt_qnt.setError(context.getString(R.string.required));
                return;
            }else if(etxt_qnt.getText().toString().equals("")){
                etxt_qnt.setError(context.getString(R.string.required));
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
                            Toast.makeText(context, "Plant Added to your shop successfully", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,category;
        ImageView image;
        Button sell,details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            details = itemView.findViewById(R.id.details);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            sell = itemView.findViewById(R.id.sell);

        }
    }
}
