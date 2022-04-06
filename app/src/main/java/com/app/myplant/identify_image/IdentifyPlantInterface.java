package com.app.myplant.identify_image;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IdentifyPlantInterface {

    @POST("identify_sample")
    Call<ImageUploadResponse> uploadImage(@Body UploadImageRequest uploadImageRequest);

    @GET("get_identification/{id}")
    Call<ImageIdenifyResponse> identifyImage(
            @Path("id") String secret
    );

}
