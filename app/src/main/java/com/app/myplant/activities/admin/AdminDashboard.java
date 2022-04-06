package com.app.myplant.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.myplant.R;
import com.app.myplant.activities.MainActivity;
import com.app.myplant.helper.SharedData;

public class AdminDashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        (findViewById(R.id.plant_types))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminDashboard.this,PlantCategoryListActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.plants))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminDashboard.this,PlantsListAcitivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.complaints))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminDashboard.this,ComplaintListActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.instructions))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedData.isUser = false;
                        Intent intent = new Intent(AdminDashboard.this,InstructionsListActivity.class);
                        startActivity(intent);
                    }
                });

        (findViewById(R.id.farmers))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminDashboard.this,FarmerAccountsActivity.class);
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

                        Intent intent = new Intent(AdminDashboard.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}