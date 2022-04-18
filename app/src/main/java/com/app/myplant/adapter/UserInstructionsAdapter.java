package com.app.myplant.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.activities.admin.AddInstuctionActivity;
import com.app.myplant.activities.user.InstructionActivity;
import com.app.myplant.callback.InstructionCallback;
import com.app.myplant.controllers.InstructionController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Instruction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserInstructionsAdapter extends RecyclerView.Adapter<UserInstructionsAdapter.ViewHolder> {

    private ArrayList<Instruction> Plants;
    private Context context;

    public UserInstructionsAdapter(ArrayList<Instruction> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.inst_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instruction Plant = Plants.get(position);

        holder.title.setText(Plant.getTitle());
        holder.description.setText("");
        if(Plant.getPlantCategory() != null)
            holder.category.setText(Plant.getPlantCategory().getName());
        else
            holder.category.setText("");

        Picasso.get()
                .load(Plant.getImageURL())
                .into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.userInstruction = Plant;
                Intent intent = new Intent(context, InstructionActivity.class);
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);

        }
    }
}
