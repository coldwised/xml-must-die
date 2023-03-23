package com.example.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.data.remote.ImagesApi;
import com.example.domain.repository.Repository;
import com.example.mdc.R;
import com.example.mdc.util.Resource;
import com.example.mdc.util.UiText;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepositoryImpl implements Repository {

    public @Inject RepositoryImpl(@NotNull ImagesApi api) {
        this.api = api;
    }

    @NotNull
    private final ImagesApi api;
    @Override
    @NotNull
    public LiveData<Resource<ArrayList<String>>> getImagesUrlList() {
        MutableLiveData<Resource<ArrayList<String>>> result = new MutableLiveData<>();
        try {
            api
                    .loadFile()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe( data ->
                            {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(data.byteStream()));
                                String line;
                                ArrayList<String> list = new ArrayList<>();
                                while ((line = reader.readLine()) != null) {
                                    list.add(line);
                                }
                                result.postValue(new Resource.Success<>(list));
                            }

                    )
                    ;
        } catch (Exception e) {
            result.postValue(new Resource.Error<>(new UiText.StringResource(R.string.io_exception))
            );
        }
        return result;
    }
}
