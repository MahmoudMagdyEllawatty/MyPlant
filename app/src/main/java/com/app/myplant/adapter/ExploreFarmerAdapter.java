package com.app.myplant.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.activities.user.ChatDetailsActivity;
import com.app.myplant.activities.user.ChatsActivity;
import com.app.myplant.activities.user.ExploreFarmersActivity;
import com.app.myplant.activities.user.UserDashboard;
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.callback.OrderCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.controllers.OrderController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Chat;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Order;
import com.app.myplant.model.Plant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class ExploreFarmerAdapter extends RecyclerView.Adapter<ExploreFarmerAdapter.ViewHolder> {

    int sDay = 0, sMonth = 0,sYear=0;
    private ArrayList<FarmerPlant> Plants;
    private Context context;

    public ExploreFarmerAdapter(ArrayList<FarmerPlant> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_explore_farmer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FarmerPlant Plant = Plants.get(position);

        holder.title.setText(Plant.getFarmer().getShopName());
        holder.category.setText(Plant.getFarmer().getName());
        holder.description.setText(String.format("%s KWD", Plant.getPrice()));

        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDialog(Plant);
            }
        });

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat();
                chat.setChats(new ArrayList<>());
                chat.setFarmer(Plant.getFarmer());
                chat.setKey("");
                chat.setUser(SharedData.loggedUser);

                SharedData.chat = chat;
                Intent intent = new Intent(context, ChatDetailsActivity.class);
                context.startActivity(intent);
            }
        });

    }


    private void showOrderDialog(FarmerPlant plant){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_order,null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        final Button dialogBtnSubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageButton dialogBtnClose = dialogView.findViewById(R.id.btn_close);
        final EditText etxt_price = dialogView.findViewById(R.id.etxt_price); // date
        final EditText etxt_qnt = dialogView.findViewById(R.id.etxt_qnt); // time
        final TextView title = dialogView.findViewById(R.id.title);


        title.setText(new StringBuilder().append("Order ").append(plant.getPlant().getName()).toString());

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        etxt_price.setFocusable(false);
        etxt_qnt.setFocusable(false);

        Calendar calendar = Calendar.getInstance();
        sDay = calendar.get(Calendar.DAY_OF_MONTH);
        sMonth = calendar.get(Calendar.MONTH);
        sYear = calendar.get(Calendar.YEAR);

        int sHour = calendar.get(Calendar.HOUR_OF_DAY);
        int sMinute = calendar.get(Calendar.MINUTE);

        etxt_price.setShowSoftInputOnFocus(false);
        etxt_price.setOnClickListener(v -> fromDatePicker(sDay,sMonth,sYear,etxt_price));
        etxt_price.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fromDatePicker(sDay,sMonth,sYear,etxt_price);
            }
        });


        etxt_qnt.setShowSoftInputOnFocus(false);
        etxt_qnt.setOnClickListener(v -> fromHourPicker(sHour,sMinute,etxt_qnt));
        etxt_qnt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                fromHourPicker(sHour,sMinute,etxt_qnt);
            }
        });


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


            Order order  = new Order();
            order.setState(0);
            order.setDate(etxt_price.getText().toString());
            order.setFarmerPlant(plant);
            order.setKey("");
            order.setTime(etxt_qnt.getText().toString());
            order.setUser(SharedData.loggedUser);


            new OrderController().Save(order, new OrderCallback() {
                @Override
                public void onSuccess(ArrayList<Order> chats) {
                    Toast.makeText(context, "Order Sent Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, UserDashboard.class);
                    context.startActivity(intent);
                }

                @Override
                public void onFail(String msg) {

                }
            });


        });


        dialogBtnClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void fromDatePicker(int sDay,int sMonth,int sYear,EditText fromHour) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();

        DatePickerDialog pickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fromHour.setText(String.format(Locale.ENGLISH,"%d/%d/%d", dayOfMonth, month+1,year));
            }
        },sYear,sMonth,sDay);
        pickerDialog.show();
    }


    private void fromHourPicker(int sHour,int sMinute,EditText fromHour) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();

        TimePickerDialog pickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                fromHour.setText(String.format(Locale.ENGLISH,"%d:%d", i,i1));
            }
        },sHour,sMinute,true);
        pickerDialog.show();
    }



    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,category;
        ImageView image;
        Button sell,chat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            sell = itemView.findViewById(R.id.sell);
            chat = itemView.findViewById(R.id.chat);

        }
    }
}
