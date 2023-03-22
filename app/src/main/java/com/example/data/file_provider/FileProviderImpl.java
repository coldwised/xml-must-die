package com.example.data.file_provider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.domain.file_provider.FileProvider;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlin.jvm.internal.Intrinsics;

import java.io.File;
import javax.inject.Inject;

public final class FileProviderImpl implements FileProvider {
    @NotNull
    private final Context context;

    @NonNull
    public File getFile() {
        return new File(this.context.getFilesDir() + "/images");
    }

    @Inject
    public FileProviderImpl(@ApplicationContext @NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }
}