package com.app.myplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.myplant.R;
import com.app.myplant.activities.user.UserDashboard;
import com.app.myplant.callback.UserCallback;
import com.app.myplant.controllers.UserController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.User;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password;
    ImageView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setTitle("Register");

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login    = findViewById(R.id.login);

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

                if(name.getText() == null){
                    name.setError("Required");
                    return;
                }else if(name.getText().toString().equals("")){
                    name.setError("Required");
                    return;
                }


                User user = new User();
                user.setKey("");
                user.setEmail(email.getText().toString());
                user.setName(name.getText().toString());
                user.setPassword(password.getText().toString());


                new UserController().Save(user, new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<User> chats) {
                        SharedData.userType = 3;
                        SharedData.loggedUser = chats.get(0);
                        Intent intent = new Intent(RegisterActivity.this, UserDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });

            }
        });
    }
}