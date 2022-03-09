package com.app.myplant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.app.myplant.activities.admin.PlantDataActivity;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private ArrayList<Plant> Plants;
    private Context context;

    public PlantAdapter(ArrayList<Plant> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.plant_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant Plant = Plants.get(position);

        holder.title.setText(Plant.getName());
        holder.category.setText(Plant.getCategory().getName());
        holder.description.setText(Plant.getDetails());

        Picasso.get()
                .load(Plant.getImageURL())
                .into(holder.image);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadingHelper(context)
                        .showDialog("Delete Category", "Are You Sure?", "Delete", "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new PlantController().delete(Plant, new PlantCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<Plant> Plants) {
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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.plant = Plant;
                Intent intent = new Intent(context, PlantDataActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,category;
        ImageView delete,edit,image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            delete = itemView.findViewById(R.id.img_delete);
            edit = itemView.findViewById(R.id.img_edit);

        }
    }
}
