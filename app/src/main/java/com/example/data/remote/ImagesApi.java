package com.example.data.remote;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
public interface ImagesApi {

    @NotNull
    String BASE_URL = "https://it-link.ru/test/";

    @NotNull
    @GET("images.txt")
    @Streaming
    Call<ResponseBody> loadFile();
}
