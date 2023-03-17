package com.example.domain.repository

import com.example.mdc.util.Resource
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

interface Repository {
    fun getImagesUrlList(): Flow<Resource<List<String>>>
}