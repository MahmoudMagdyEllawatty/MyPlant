package com.app.myplant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyPlantsAdapter extends RecyclerView.Adapter<MyPlantsAdapter.ViewHolder> {

    private ArrayList<FarmerPlant> Plants;
    private Context context;

    public MyPlantsAdapter(ArrayList<FarmerPlant> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_plant_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FarmerPlant Plant = Plants.get(position);

        holder.title.setText(Plant.getPlant().getName());
        holder.category.setText(Plant.getPlant().getCategory().getName());
        holder.description.setText(Plant.getPlant().getDetails());
        holder.price.setText(Plant.getPrice()+" KWD");

        Picasso.get()
                .load(Plant.getPlant().getImageURL())
                .into(holder.image);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSellDialog(Plant);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadingHelper(context)
                        .showDialog("Delete Plant",
                                "are you sure?",
                                "Delete",
                                "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new FarmerPlantController()
                                                .delete(Plant, new FarmerPlantCallback() {
                                                    @Override
                                                    public void onSuccess(ArrayList<FarmerPlant> chats) {
                                                        Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFail(String msg) {

                                                    }
                                                });
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
            }
        });



    }

    private void showSellDialog(FarmerPlant plant){
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


        title.setText(new StringBuilder().append("Sell ").append(plant.getPlant().getName()).toString());

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        etxt_price.setText(String.format("%s", plant.getPrice()));
        etxt_qnt.setText(String.format("%s", plant.getQnt()));

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




            plant.setPrice(Double.parseDouble(etxt_price.getText().toString()));
            plant.setQnt(Double.parseDouble(etxt_qnt.getText().toString()));


            new FarmerPlantController()
                    .Save(plant, new FarmerPlantCallback() {
                        @Override
                        public void onSuccess(ArrayList<FarmerPlant> chats) {
                            Toast.makeText(context, "Plant Added to your shop successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });



        });


        dialogBtnClose.setOnClickListener(v -> alertDialog.dismiss());
    }



    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,category,price;
        ImageView image,edit,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);

            edit = itemView.findViewById(R.id.img_edit);
            delete = itemView.findViewById(R.id.img_delete);

        }
    }
}
