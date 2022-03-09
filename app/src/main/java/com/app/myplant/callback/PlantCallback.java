package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Plant;

import java.util.ArrayList;

public interface PlantCallback {
    void onSuccess(ArrayList<Plant> chats);
    void onFail(String msg);
}
