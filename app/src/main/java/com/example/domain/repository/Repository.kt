package com.example.domain.repository

import com.example.mdc.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getImagesUrlList(): Flow<Resource<List<String>>>
}