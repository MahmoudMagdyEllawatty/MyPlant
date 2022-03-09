package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Farmer;

import java.util.ArrayList;

public interface FarmerCallback {
    void onSuccess(ArrayList<Farmer> chats);
    void onFail(String msg);
}
