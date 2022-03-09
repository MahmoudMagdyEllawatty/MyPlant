package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Complaint;

import java.util.ArrayList;

public interface ComplaintCallback {
    void onSuccess(ArrayList<Complaint> chats);
    void onFail(String msg);
}
