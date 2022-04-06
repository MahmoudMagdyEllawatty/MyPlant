package com.app.myplant.identify_image;

import com.google.gson.annotations.SerializedName;

public class ImageUploadResponse {
    @SerializedName("secret")
    public String secret;

    public String getSecret() {
        return secret;
    }
}
