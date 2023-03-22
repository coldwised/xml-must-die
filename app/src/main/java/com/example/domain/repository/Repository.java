package com.example.domain.repository;

import androidx.lifecycle.LiveData;

import com.example.mdc.util.Resource;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import kotlinx.coroutines.flow.Flow;

public interface Repository {
    LiveData<Resource<ArrayList<String>>> getImagesUrlList();
}
