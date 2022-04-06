package com.app.myplant.identify_image;

import com.google.gson.annotations.SerializedName;

public class UploadImageRequest {
    @SerializedName("images")
    public String[] images;

    @SerializedName("modifiers")
    public String[] modifiers;

    public String[] getImages() {
        return images;
    }

    public String[] getModifiers() {
        return modifiers;
    }
}
