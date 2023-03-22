package com.example.di;

import com.example.data.remote.ImagesApi;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn({SingletonComponent.class})
public final class AppModule {

    @Provides
    @Singleton
    @NotNull
    public ImagesApi provideInterviewApplicationApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ImagesApi.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ImagesApi.class);
    }
}
