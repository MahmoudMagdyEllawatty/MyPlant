package com.app.myplant.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myplant.R;
import com.app.myplant.callback.ChatCallback;
import com.app.myplant.controllers.ChatController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Chat;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatDetailsActivity extends AppCompatActivity {


    RecyclerView messagesList;

    EditText message;
    ImageView send;

    ArrayList<Chat.ChatDetails> chatDetails = new ArrayList<>();
    int side = 0;

    LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        loadingHelper = new LoadingHelper(this);
        if( SharedData.loggedFarmer != null){
            side = 2;
            getSupportActionBar().setTitle(SharedData.chat.getUser().getName());
        }else if(SharedData.loggedUser != null){
            side = 1;
            getSupportActionBar().setTitle(SharedData.chat.getFarmer().getName());
        }


        messagesList = findViewById(R.id.messages_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setItemAnimator(new DefaultItemAnimator());


        message = findViewById(R.id.message);
        send = findViewById(R.id.send);


        chatDetails = SharedData.chat.getChats();

        messagesList.setAdapter(new ChatMessageAdapter());

        SharedData.mCurrentIndex.observe(this, new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                chatDetails = SharedData.chat.getChats();
                messagesList.setAdapter(new ChatMessageAdapter());
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message.getText() == null){
                    return;
                }else if(message.getText().toString().equals("")){
                    return;
                }else{
                    Chat.ChatDetails newMsg = new Chat.ChatDetails();
                    newMsg.setDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
                    newMsg.setFilePath("");
                    newMsg.setMsg(message.getText().toString());
                    newMsg.setSide(side);

                    ArrayList<Chat.ChatDetails> chatDetailsArrayList = SharedData.chat.getChats();
                    chatDetailsArrayList.add(newMsg);

                    SharedData.chat.setChats(chatDetailsArrayList);

                    messagesList.setAdapter(new ChatMessageAdapter());
                    new ChatController().Save(SharedData.chat, new ChatCallback() {
                        @Override
                        public void onSuccess(ArrayList<Chat> children) {
                            message.setText("");
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(ChatDetailsActivity.this)
                    .inflate(R.layout.chat_message_row,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chat.ChatDetails detail = chatDetails.get(position);

            if(side == detail.getSide()){
                holder.relativeLayout.setVisibility(View.VISIBLE);
                holder.relativeLayout1.setVisibility(View.GONE);
            }else{
                holder.relativeLayout1.setVisibility(View.VISIBLE);
                holder.relativeLayout.setVisibility(View.GONE);
            }

            holder.message.setText(detail.getMsg());
            holder.date.setText(detail.getDate());

            holder.message1.setText(detail.getMsg());
            holder.date1.setText(detail.getDate());


        }

        @Override
        public int getItemCount() {
            return chatDetails.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            LinearLayout relativeLayout;
            TextView message,date;

            LinearLayout relativeLayout1;
            TextView message1,date1;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                relativeLayout = itemView.findViewById(R.id.my_side);
                message = itemView.findViewById(R.id.message);
                date = itemView.findViewById(R.id.date);


                relativeLayout1 = itemView.findViewById(R.id.his_side);
                message1 = itemView.findViewById(R.id.message1);
                date1 = itemView.findViewById(R.id.date1);
            }
        }
    }

}