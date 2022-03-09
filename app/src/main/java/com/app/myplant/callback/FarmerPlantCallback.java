package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.FarmerPlant;

import java.util.ArrayList;

public interface FarmerPlantCallback {
    void onSuccess(ArrayList<FarmerPlant> chats);
    void onFail(String msg);
}
