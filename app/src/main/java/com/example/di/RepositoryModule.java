package com.example.di;

import com.example.data.file_provider.FileProviderImpl;
import com.example.data.repository.RepositoryImpl;
import com.example.domain.file_provider.FileProvider;
import com.example.domain.repository.Repository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract Repository bindRepository(RepositoryImpl repositoryImpl);

    @Binds
    @Singleton
    abstract FileProvider bindFileProvider(FileProviderImpl fileProviderImpl);
}
