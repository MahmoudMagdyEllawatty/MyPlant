package com.app.myplant.controllers;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.app.myplant.callback.FileUploadCallback;
import com.app.myplant.helper.FirebaseHelper;
import com.app.myplant.model.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ImageController {

    FirebaseHelper<Plant> helper = new FirebaseHelper<>();

    public void uploadImage(Uri uri, final FileUploadCallback callback){
        helper.uploadDoc(uri.toString(),uri)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            callback.onSuccess(task.getResult().toString());
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }

}
