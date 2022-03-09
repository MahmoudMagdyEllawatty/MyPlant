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
import com.app.myplant.activities.admin.PlantDataActivity;
import com.app.myplant.activities.user.ChatDetailsActivity;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Chat;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Chat> Plants;
    private Context context;

    public ChatAdapter(ArrayList<Chat> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.chat_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat Plant = Plants.get(position);

        if(SharedData.loggedFarmer == null){
            holder.title.setText(Plant.getFarmer().getShopName());
        }else{
            holder.title.setText(Plant.getUser().getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData.chat = Plant;
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);

        }
    }
}
