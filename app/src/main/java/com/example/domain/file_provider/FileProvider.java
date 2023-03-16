package com.example.domain.file_provider;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface FileProvider {
    @NotNull
    File getFile();
}
