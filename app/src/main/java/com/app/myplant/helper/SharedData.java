package com.app.myplant.helper;

import androidx.lifecycle.MutableLiveData;

import com.app.myplant.model.Chat;
import com.app.myplant.model.Farmer;
import com.app.myplant.model.Instruction;
import com.app.myplant.model.Plant;
import com.app.myplant.model.User;

import java.io.File;

public class SharedData {
    public static int userType = 0;



    public static File last_file;
    public static Farmer loggedFarmer;
    public static User loggedUser;
    public static Plant plant;
    public static Instruction instruction;
    public static Farmer farmer;
    public static Plant current_plant;
    public static Chat chat;

    public static MutableLiveData<Chat> mCurrentIndex = new MutableLiveData<>();

    public static String secret;
    public static boolean isUser;
    public static Plant infoPlant;
    public static boolean isSell;
    public static Instruction userInstruction;
}
