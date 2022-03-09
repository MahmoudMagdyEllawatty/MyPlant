package com.app.myplant.activities.farmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.myplant.R;
import com.app.myplant.activities.MainActivity;
import com.app.myplant.activities.admin.AdminDashboard;
import com.app.myplant.activities.admin.FarmerDataActivity;
import com.app.myplant.activities.user.ChatsActivity;
import com.app.myplant.helper.SharedData;

public class FarmerDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        (findViewById(R.id.my_plants))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FarmerDashboard.this,MyPlantsActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.my_orders))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FarmerDashboard.this,OrdersActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.plants))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FarmerDashboard.this,ExplorePlantsActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.chat))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FarmerDashboard.this, ChatsActivity.class);
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

                        Intent intent = new Intent(FarmerDashboard.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}