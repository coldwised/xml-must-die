package com.example.data.remote;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
public interface ImagesApi {

    @NotNull
    String BASE_URL = "https://it-link.ru/test/";

    @NotNull
    @GET("images.txt")
    Observable<ResponseBody> loadFile();
}
