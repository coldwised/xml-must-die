package com.example.data.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
public interface ImagesApi {

    String BASE_URL = "https://it-link.ru/test/";

    @GET("images.txt")
    @Streaming
    Call<ResponseBody> loadFile();
}
