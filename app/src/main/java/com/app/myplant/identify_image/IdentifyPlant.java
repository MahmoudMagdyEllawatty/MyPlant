package com.app.myplant.identify_image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IdentifyPlant {
    //to upload image
    //url => https://plant.id/api_frontend/identify_sample
    //type => POST
    //params => images[] , modifiers[]
            // images is array of image to upload
            // modifiers is array of strings with only one value "similar_images"
    //response => "secret" to get image secret to use in next step



    //to identify image
    //url => https://plant.id/api_frontend/get_identification/secret
    //type => get
    //params => no
    //response => "suggestions" => list of suggestions
                //=> plant_name,probability,plant_details
                //plant_details has => name_authority,wiki_image,wiki_description
                //wiki_image => has value
                //wiki_description => has value

    private static final String BASE_URL = "https://plant.id/api_frontend/";
    private static Retrofit retrofit = null;




    public static Retrofit getApiClient() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }

}
