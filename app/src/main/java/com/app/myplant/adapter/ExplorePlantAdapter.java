package com.app.myplant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.activities.farmer.PlantDetailsActivity;
import com.app.myplant.activities.user.ExploreFarmersActivity;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ExplorePlantAdapter extends RecyclerView.Adapter<ExplorePlantAdapter.ViewHolder> {

    private ArrayList<Plant> Plants;
    private Context context;

    public ExplorePlantAdapter(ArrayList<Plant> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_explore_plants,parent,false);
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



        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.current_plant = Plant;
                Intent intent = new Intent(context, ExploreFarmersActivity.class);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.infoPlant = Plant;
                Intent intent = new Intent(context, PlantDetailsActivity.class);
                context.startActivity(intent);
            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.infoPlant = Plant;
                Intent intent = new Intent(context, PlantDetailsActivity.class);
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
