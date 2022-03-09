package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Order;

import java.util.ArrayList;

public interface OrderCallback {
    void onSuccess(ArrayList<Order> chats);
    void onFail(String msg);
}
