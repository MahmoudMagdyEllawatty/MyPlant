package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.PlantCategory;

import java.util.ArrayList;

public interface PlantCategoryCallback {
    void onSuccess(ArrayList<PlantCategory> chats);
    void onFail(String msg);
}
