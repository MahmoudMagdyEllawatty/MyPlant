package com.app.myplant.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myplant.R;
import com.app.myplant.callback.PlantCategoryCallback;
import com.app.myplant.controllers.PlantCategoryController;
import com.app.myplant.helper.LoadingHelper;
import com.app.myplant.model.PlantCategory;

import java.util.ArrayList;
import java.util.Locale;


public class PlantCategoriesAdapter extends RecyclerView.Adapter<PlantCategoriesAdapter.ViewHolder> {

    private ArrayList<PlantCategory> PlantCategorys;
    private Context context;

    public PlantCategoriesAdapter(ArrayList<PlantCategory> PlantCategorys, Context context) {
        this.PlantCategorys = PlantCategorys;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.category_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantCategory PlantCategory = PlantCategorys.get(position);

        holder.title.setText(PlantCategory.getName());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadingHelper(context)
                        .showDialog("Delete Category", "Are You Sure?", "Delete", "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new PlantCategoryController().delete(PlantCategory, new PlantCategoryCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<PlantCategory> PlantCategorys) {
                                        Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFail(String msg) {

                                    }
                                });
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStationDialog(PlantCategory);
            }
        });

    }

    private void showAddStationDialog(PlantCategory PlantCategory){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        View dialogView =  LayoutInflater.from(context).inflate(R.layout.dialog_station, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        final Button dialogBtnSubmit = dialogView.findViewById(R.id.btn_submit);
        final ImageButton dialogBtnClose = dialogView.findViewById(R.id.btn_close);
        final EditText etxtTitle = dialogView.findViewById(R.id.etxt_title);
        final TextView title = dialogView.findViewById(R.id.title);

        etxtTitle.setText(PlantCategory.getName());


        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialogBtnSubmit.setOnClickListener(v -> {

            if(etxtTitle.getText() == null){
                etxtTitle.setError(context.getString(R.string.required));
                return;
            }else if(etxtTitle.getText().toString().equals("")){
                etxtTitle.setError(context.getString(R.string.required));
                return;
            }


            PlantCategory.setName(etxtTitle.getText().toString());

            new PlantCategoryController()
                    .Save(PlantCategory, new PlantCategoryCallback() {
                        @Override
                        public void onSuccess(ArrayList<PlantCategory> policies) {
                            alertDialog.dismiss();
                            Toast.makeText(context, "Edited Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(String msg) {
                            alertDialog.dismiss();
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    });


        });


        dialogBtnClose.setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    public int getItemCount() {
        return PlantCategorys.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView delete,edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.img_delete);
            edit = itemView.findViewById(R.id.img_edit);

        }
    }
}
