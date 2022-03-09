package com.app.myplant.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.activities.admin.PlantCategoryListActivity;
import com.app.myplant.activities.admin.PlantDataActivity;
import com.app.myplant.activities.admin.PlantsListAcitivity;
import com.app.myplant.adapter.ComplaintAdapter;
import com.app.myplant.adapter.PlantAdapter;
import com.app.myplant.callback.ComplaintCallback;
import com.app.myplant.callback.PlantCallback;
import com.app.myplant.callback.PlantCategoryCallback;
import com.app.myplant.controllers.ComplaintController;
import com.app.myplant.controllers.PlantCategoryController;
import com.app.myplant.controllers.PlantController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Complaint;
import com.app.myplant.model.Farmer;
import com.app.myplant.model.Plant;
import com.app.myplant.model.PlantCategory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserComplaintsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    ImageView imgNoProduct;
    TextView no_data_fount;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complaints);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.complaints_list);


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
                showAddComplaintDialog();
            }
        });
    }


    private void showAddComplaintDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserComplaintsActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_complaint, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        final Button dialogBtnSubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageButton dialogBtnClose = dialogView.findViewById(R.id.btn_close);
        final EditText etxtTitle = dialogView.findViewById(R.id.etxt_title);
        final EditText etxtText = dialogView.findViewById(R.id.etxt_text);
        final TextView title = dialogView.findViewById(R.id.title);


        title.setText(R.string.complaints_list);


        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialogBtnSubmit.setOnClickListener(v -> {

            if(etxtTitle.getText() == null){
                etxtTitle.setError(getString(R.string.required));
                return;
            }else if(etxtTitle.getText().toString().equals("")){
                etxtTitle.setError(getString(R.string.required));
                return;
            }

            if(etxtText.getText() == null){
                etxtText.setError(getString(R.string.required));
                return;
            }else if(etxtText.getText().toString().equals("")){
                etxtText.setError(getString(R.string.required));
                return;
            }

            Complaint plantCategory = new Complaint();
            plantCategory.setReply("");
            plantCategory.setKey("");
            plantCategory.setText(etxtText.getText().toString());
            plantCategory.setUser(SharedData.loggedUser);
            plantCategory.setTitle(etxtTitle.getText().toString());

            new ComplaintController()
                    .Save(plantCategory, new ComplaintCallback() {
                        @Override
                        public void onSuccess(ArrayList<Complaint> policies) {
                            alertDialog.dismiss();
                            Toast.makeText(UserComplaintsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String msg) {
                            alertDialog.dismiss();
                            Toast.makeText(UserComplaintsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });


        });


        dialogBtnClose.setOnClickListener(v -> alertDialog.dismiss());
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
        new ComplaintController().getComplaints(new ComplaintCallback() {
            @Override
            public void onSuccess(ArrayList<Complaint> complaints) {
                ArrayList<Complaint> banks = new ArrayList<>();
                for (Complaint complaint : complaints){
                    if(complaint.getUser().getKey().equals(SharedData.loggedUser.getKey())){
                        banks.add(complaint);
                    }
                }
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

                    recyclerView.setAdapter(new ComplaintAdapter(banks, UserComplaintsActivity.this));
                }
            }

            @Override
            public void onFail(String msg) {
                //Stopping Shimmer Effects
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                imgNoProduct.setVisibility(View.GONE);
                Toast.makeText(UserComplaintsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}