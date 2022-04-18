package com.app.myplant.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.myplant.R;
import com.app.myplant.activities.MainActivity;
import com.app.myplant.activities.admin.AdminDashboard;
import com.app.myplant.activities.admin.InstructionsListActivity;
import com.app.myplant.activities.farmer.FarmerDashboard;
import com.app.myplant.activities.farmer.OrdersActivity;
import com.app.myplant.helper.SharedData;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setTitle("User Dashboard");
        (findViewById(R.id.my_orders))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserDashboard.this, OrdersActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.instructions))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.isUser = true;
                        Intent intent = new Intent(UserDashboard.this, InstructionsListActivity.class);
                        startActivity(intent);
                    }
                });


        (findViewById(R.id.plants))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserDashboard.this, ExplorePlantsActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.chat))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserDashboard.this, ChatsActivity.class);
                        startActivity(intent);
                    }
                });


        (findViewById(R.id.complaints))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserDashboard.this, UserComplaintsActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.logout))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedData.loggedUser = null;
                        SharedData.loggedFarmer = null;
                        SharedData.farmer = null;
                        SharedData.userType = 0;
                        SharedData.plant = null;
                        SharedData.current_plant = null;
                        SharedData.last_file = null;

                        Intent intent = new Intent(UserDashboard.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}