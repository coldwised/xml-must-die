package com.example.mdc.util;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public interface Resource<T> {
    class Success<T> implements Resource<T> {
        public Success (@NonNull T data) {
            this.data = data;
        }
        @NotNull
        public final T data;
    }
    class Error<T> implements Resource<T> {
        public Error (@NonNull UiText message) {
            this.message = message;
        }
        @NotNull
        public final UiText message;
    }
}


