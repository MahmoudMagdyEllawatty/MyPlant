package com.app.myplant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.callback.ComplaintCallback;
import com.app.myplant.controllers.ComplaintController;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Complaint;

import java.util.ArrayList;


public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private ArrayList<Complaint> Plants;
    private Context context;

    public ComplaintAdapter(ArrayList<Complaint> Plants, Context context) {
        this.Plants = Plants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.complaint_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Complaint Plant = Plants.get(position);

        holder.title.setText(Plant.getTitle());

        holder.user.setText(Plant.getText());

        if (Plant.getReply().equals("")) {
            if(SharedData.userType == 1){
                holder.replyText.setVisibility(View.VISIBLE);
                holder.replyText.setVisibility(View.VISIBLE);
                holder.send_reply.setVisibility(View.VISIBLE);
            }else{
                holder.send_reply.setVisibility(View.GONE);
                holder.reply.setVisibility(View.GONE);
                holder.replyText.setVisibility(View.GONE);
            }
        }else{
            holder.send_reply.setVisibility(View.GONE);
            holder.replyText.setVisibility(View.GONE);
            holder.reply.setText("Reply : "+Plant.getReply());
        }



        holder.send_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Plant.setReply(holder.replyText.getText().toString());

                new ComplaintController()
                        .Save(Plant, new ComplaintCallback() {
                            @Override
                            public void onSuccess(ArrayList<Complaint> chats) {
                                Toast.makeText(context, "Reply Sent", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(String msg) {

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return Plants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,user,reply;
        EditText replyText;
        Button send_reply;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            send_reply = itemView.findViewById(R.id.send_reply);
            user = itemView.findViewById(R.id.user);
            reply = itemView.findViewById(R.id.txt_reply);
            replyText = itemView.findViewById(R.id.etxt_reply);
            title = itemView.findViewById(R.id.title);

        }
    }
}
