package com.app.myplant.controllers;

import androidx.annotation.NonNull;

import com.app.myplant.callback.ChatCallback;
import com.app.myplant.helper.Config;
import com.app.myplant.helper.FirebaseHelper;
import com.app.myplant.helper.SharedData;
import com.app.myplant.model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ChatController {
    private final String node = Config.CHAT_NODE;
    ArrayList<Chat> users = new ArrayList<>();
    FirebaseHelper<Chat> helper = new FirebaseHelper<Chat>();

    public void Save(final Chat user, final ChatCallback callback){
        if(user.getKey().equals("")){
            user.setKey(helper.getKey(node));
        }

        helper.save(node,user.getKey(),user)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            users.add(user);
                            callback.onSuccess(users);
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }

    public void delete(Chat trainingType, ChatCallback callback){
        helper.delete(node, trainingType.getKey())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            users = new ArrayList<>();
                            callback.onSuccess(users);
                        }else{
                            callback.onFail(task.getException().toString());
                        }
                    }
                });
    }

    public void getChats(final ChatCallback callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<Chat> users = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        Chat user = snap.toObject(Chat.class);
                        if(user.getFarmer() == null)
                            continue;
                        if(user.getUser().equals(""))
                            continue;

                        if(SharedData.loggedFarmer != null){
                            if(user.getFarmer().getKey().equals(SharedData.loggedFarmer.getKey()))
                                users.add(user);
                        }else{
                            if(user.getUser().getKey().equals(SharedData.loggedUser.getKey()))
                                users.add(user);
                        }

                        if(SharedData.chat != null){
                            if(SharedData.chat.getKey().equals(user.getKey())){
                                SharedData.chat = user;
                                SharedData.mCurrentIndex.setValue(user);
                            }
                        }


                    }
                    callback.onSuccess(users);
                }
            }
        });
    }
}
