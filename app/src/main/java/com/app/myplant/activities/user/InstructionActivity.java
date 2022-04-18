package com.app.myplant.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.myplant.R;
import com.app.myplant.helper.SharedData;
import com.squareup.picasso.Picasso;

public class InstructionActivity extends AppCompatActivity {

    ImageView image;
    TextView title,description,category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Instruction");

        image   = findViewById(R.id.image);
        title = findViewById(R.id.title);
        description = findViewById(R.id.details);
        category = findViewById(R.id.plant_category);


        Picasso.get()
                .load(SharedData.userInstruction.getImageURL())
                .into(image);


        title.setText(SharedData.userInstruction.getTitle());
        description.setText(SharedData.userInstruction.getDescription());
        category.setText(SharedData.userInstruction.getPlantCategory().getName());

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