package com.app.myplant.controllers;

import androidx.annotation.NonNull;

import com.app.myplant.callback.UserCallback;
import com.app.myplant.helper.Config;
import com.app.myplant.helper.FirebaseHelper;
import com.app.myplant.model.Farmer;
import com.app.myplant.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class UserController {
    private final String node = Config.USER_NODE;
    ArrayList<User> users = new ArrayList<>();
    FirebaseHelper<User> helper = new FirebaseHelper<User>();

    public void Save(final User user, final UserCallback callback){
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

    public void delete(User trainingType, UserCallback callback){
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

    public void getUsers(final UserCallback callback){
        helper.getAll(node, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null && e != null){
                    callback.onFail(e.getLocalizedMessage());
                }else {
                    ArrayList<User> users = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                        User user = snap.toObject(User.class);
                        users.add(user);
                    }
                    callback.onSuccess(users);
                }
            }
        });
    }

    public void checkLogin(String email,String password, final UserCallback callback){
        helper.getAllOnce(node)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<User> users = new ArrayList<>();
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            User user = snap.toObject(User.class);
                            if(user.getEmail().equals(email) && user.getPassword().equals(password))
                                users.add(user);
                        }
                        callback.onSuccess(users);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFail(e.toString());
            }
        });
    }



}
