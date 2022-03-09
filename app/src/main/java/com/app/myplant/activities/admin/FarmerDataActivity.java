package com.app.myplant.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.FarmerCallback;
import com.app.myplant.controllers.FarmerController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Farmer;

import java.util.ArrayList;

public class FarmerDataActivity extends AppCompatActivity {

    EditText name,email,password,shopName;
    TextView save;
    Farmer farmer;
    LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_data);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.farmer_data);

        name = findViewById(R.id.etxt_name);
        email = findViewById(R.id.etxt_email);
        password = findViewById(R.id.etxt_password);
        shopName = findViewById(R.id.etxt_shop_name);
        save = findViewById(R.id.txt_save);

        loadingHelper = new LoadingHelper(this);

        if(SharedData.farmer == null){
            farmer = new Farmer();
            farmer.setKey("");
        }else{
            farmer = SharedData.farmer;
            name.setText(farmer.getName());
            email.setText(farmer.getEmail());
            password.setText(farmer.getPassword());
            shopName.setText(farmer.getShopName());
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText() == null){
                    name.setError("Required");
                    return;
                }else if(name.getText().toString().equals("")){
                    name.setError("Required");
                    return;
                }

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

                if(shopName.getText() == null){
                    shopName.setError("Required");
                    return;
                }else if(shopName.getText().toString().equals("")){
                    shopName.setError("Required");
                    return;
                }


                farmer.setEmail(email.getText().toString());
                farmer.setName(name.getText().toString());
                farmer.setPassword(password.getText().toString());
                farmer.setShopName(shopName.getText().toString());

                loadingHelper.showLoading("Saving Data");
                new FarmerController().Save(farmer, new FarmerCallback() {
                    @Override
                    public void onSuccess(ArrayList<Farmer> chats) {
                        loadingHelper.dismissLoading();
                        Toast.makeText(FarmerDataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });


            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}