package com.app.myplant.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.activities.admin.ComplaintListActivity;
import com.app.myplant.adapter.ExplorePlantAdapter;
import com.app.myplant.adapter.SellPlantAdapter;
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Plant;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ExplorePlantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ImageView imgNoProduct;
    TextView no_data_fount;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<Plant> allPlants,filteredPlants;
    EditText search;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_plants);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.explore_plants);


        add = findViewById(R.id.fab_add);
        no_data_fount = findViewById(R.id.no_data_fount);
        recyclerView = findViewById(R.id.cart_recyclerview);
        imgNoProduct = findViewById(R.id.image_no_product);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout =findViewById(R.id.swipeToRefresh);
        //set color of swipe refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_700);

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.setHasFixedSize(true);

        //swipe refresh listeners
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            loadData();
            //after shuffle id done then swife refresh is off
            mSwipeRefreshLayout.setRefreshing(false);
        });


        loadData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExplorePlantsActivity.this,IdentifyImageActivity.class);
                startActivity(intent);
            }
        });

        search = findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void search(String text){
        filteredPlants = new ArrayList<>();
        for (Plant plant : allPlants){
            if(plant.getName().toLowerCase().contains(text.toLowerCase()) ||
                    plant.getCategory().getName().toLowerCase().contains(text.toLowerCase()) ||
                    plant.getDetails().toLowerCase().contains(text.toLowerCase())){
                filteredPlants.add(plant);
            }
        }
        recyclerView.setAdapter(new ExplorePlantAdapter(filteredPlants,
                ExplorePlantsActivity.this));
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

    private void loadData(){
        new PlantController().getPlants(new PlantCallback() {
            @Override
            public void onSuccess(ArrayList<Plant> banks) {
                allPlants = banks;
                if(banks.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    no_data_fount.setVisibility(View.VISIBLE);
                    no_data_fount.setText(R.string.no_result_found);
                    imgNoProduct.setVisibility(View.VISIBLE);
                    imgNoProduct.setImageResource(R.drawable.not_found);
                    //Stopping Shimmer Effects
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);

                }else{
                    //Stopping Shimmer Effects
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    imgNoProduct.setVisibility(View.GONE);
                    no_data_fount.setVisibility(View.GONE);

                    recyclerView.setAdapter(new ExplorePlantAdapter(banks, ExplorePlantsActivity.this));
                }
            }

            @Override
            public void onFail(String msg) {
                //Stopping Shimmer Effects
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                imgNoProduct.setVisibility(View.GONE);
                Toast.makeText(ExplorePlantsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

}