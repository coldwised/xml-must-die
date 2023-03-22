package com.example.domain.repository

import org.jetbrains.annotations.NotNull

interface Repository {
    fun getImagesUrlList(): ArrayList<String>
}