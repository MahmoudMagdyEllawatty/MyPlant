package com.app.myplant.callback;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Instruction;

import java.util.ArrayList;

public interface InstructionCallback {
    void onSuccess(ArrayList<Instruction> chats);
    void onFail(String msg);
}
