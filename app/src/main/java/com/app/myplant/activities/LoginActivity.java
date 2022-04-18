package com.app.myplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.activities.admin.AdminDashboard;
import com.app.myplant.activities.farmer.FarmerDashboard;
import com.app.myplant.activities.user.UserDashboard;
import com.app.myplant.callback.FarmerCallback;
import com.app.myplant.callback.UserCallback;
import com.app.myplant.controllers.FarmerController;
import com.app.myplant.controllers.UserController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Farmer;
import com.app.myplant.model.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    ImageView login;
    LoadingHelper loadingHelper;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setTitle("Login");

        loadingHelper = new LoadingHelper(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText() == null){
                    email.setError("Required");
                    return;
                }else if(email.getText().toString().equals("")){
                    email.setError("Required");
                    return;
                }

                if(password.getText() == null){
                    password.setError("Required");
                    return;
                }else if(password.getText().toString().equals("")){
                    password.setError("Required");
                    return;
                }


                loadingHelper.showLoading("Validating Login");
                if(email.getText().toString().equals("admin@plants.com") &&
                password.getText().toString().equals("123456")){
                    loadingHelper.dismissLoading();
                    SharedData.userType = 1;
                    Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    new FarmerController().checkLogin(email.getText().toString(),
                            password.getText().toString(), new FarmerCallback() {
                                @Override
                                public void onSuccess(ArrayList<Farmer> chats) {
                                    if(chats.size() > 0){
                                        loadingHelper.dismissLoading();
                                        SharedData.userType = 2;
                                        SharedData.loggedFarmer = chats.get(0);
                                        Intent intent = new Intent(LoginActivity.this, FarmerDashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else{
                                        new UserController().checkLogin(email.getText().toString(),
                                                password.getText().toString(), new UserCallback() {
                                                    @Override
                                                    public void onSuccess(ArrayList<User> chats) {
                                                        if(chats.size() > 0){
                                                            loadingHelper.dismissLoading();
                                                            SharedData.userType = 3;
                                                            SharedData.loggedUser = chats.get(0);
                                                            Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }else{
                                                            loadingHelper.dismissLoading();
                                                            Toast.makeText(LoginActivity.this, "invalid email or password", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFail(String msg) {
                                                        loadingHelper.dismissLoading();
                                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFail(String msg) {
                                    loadingHelper.dismissLoading();
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}