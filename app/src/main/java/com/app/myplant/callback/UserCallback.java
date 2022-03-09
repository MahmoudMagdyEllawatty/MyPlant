package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.User;

import java.util.ArrayList;

public interface UserCallback {
    void onSuccess(ArrayList<User> chats);
    void onFail(String msg);
}
