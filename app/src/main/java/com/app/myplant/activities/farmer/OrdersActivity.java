package com.app.myplant.activities.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.adapter.MyPlantsAdapter;
import com.app.myplant.adapter.OrdersAdapter;
import com.app.myplant.callback.FarmerPlantCallback;
import com.app.myplant.callback.OrderCallback;
import com.app.myplant.controllers.FarmerPlantController;
import com.app.myplant.controllers.OrderController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.FarmerPlant;
import com.app.myplant.model.Order;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ImageView imgNoProduct;
    TextView no_data_fount;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.orders);


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

        new OrderController()
                .getOrders(new OrderCallback() {
                    @Override
                    public void onSuccess(ArrayList<Order> orders) {
                        ArrayList<Order> chats = new ArrayList<>();
                        for (Order order : orders){
                            if(SharedData.loggedUser == null && SharedData.loggedFarmer != null){
                                if(order.getFarmerPlant().getFarmer().getKey().equals(SharedData.loggedFarmer.getKey())){
                                    chats.add(order);
                                }
                            }

                            else if(SharedData.loggedUser != null && SharedData.loggedFarmer == null){
                                if(order.getUser().getKey().equals(SharedData.loggedUser.getKey())){
                                    chats.add(order);
                                }
                            }

                        }


                        if(chats.isEmpty()){
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

                            recyclerView.setAdapter(new OrdersAdapter(chats, OrdersActivity.this));
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        Toast.makeText(OrdersActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



    }

}