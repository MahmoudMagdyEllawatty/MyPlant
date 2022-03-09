package com.app.myplant.callback;

import com.app.myplant.model.Chat;

import java.util.ArrayList;

public interface ChatCallback {
    void onSuccess(ArrayList<Chat> chats);
    void onFail(String msg);
}
