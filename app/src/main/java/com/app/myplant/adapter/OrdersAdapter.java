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
import com.app.myplant.callback.OrderCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.controllers.OrderController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private ArrayList<Order> Plants;
    private Context context;

    public OrdersAdapter(ArrayList<Order> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.my_order_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order Plant = Plants.get(position);

        holder.title.setText(Plant.getFarmerPlant().getPlant().getName());
        holder.description.setText(String.format("%s %s", Plant.getDate(), Plant.getTime()));
        if(SharedData.loggedFarmer == null){
            holder.category.setText(Plant.getFarmerPlant().getFarmer().getName());
        }else{
            holder.category.setText(Plant.getUser().getName());
        }

        holder.price.setText(String.format("%s KWD", Plant.getFarmerPlant().getPrice()));

        String state = "";
        if(Plant.getState() == 0){
            state = "Waiting For Reply";
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        }else if(Plant.getState() == 1){
            state = "Accepted";
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }else if(Plant.getState() == -1){
            state = "Canceled";
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        holder.state.setText(state);

        Picasso.get()
                .load(Plant.getFarmerPlant().getPlant().getImageURL())
                .into(holder.image);


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadingHelper(context)
                        .showDialog("Confirm Order",
                                "are you sure?",
                                "Confirm",
                                "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Plant.setState(1);
                                        new OrderController()
                                                .Save(Plant, new OrderCallback() {
                                                    @Override
                                                    public void onSuccess(ArrayList<Order> chats) {
                                                        Toast.makeText(context, "Confirmed!!", Toast.LENGTH_SHORT).show();
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


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadingHelper(context)
                        .showDialog("Delete Order",
                                "are you sure?",
                                "Delete",
                                "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Plant.setState(-1);
                                        new OrderController()
                                                .Save(Plant, new OrderCallback() {
                                                    @Override
                                                    public void onSuccess(ArrayList<Order> chats) {
                                                        Toast.makeText(context, "Canceled!!", Toast.LENGTH_SHORT).show();
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


        if(SharedData.loggedFarmer == null && SharedData.loggedUser != null){
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,category,price,state;
        ImageView image,edit,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            state = itemView.findViewById(R.id.state);

            edit = itemView.findViewById(R.id.img_edit);
            delete = itemView.findViewById(R.id.img_delete);

        }
    }
}
