package com.example.mdc.util;

public interface Resource<T> {
    class Success<T> implements Resource<T> {
        public Success (T data) {
            this.data = data;
        }
        public final T data;
    }
    class Error<T> implements Resource<T> {
        public Error (UiText message) {
            this.message = message;
        }
        public final UiText message;
    }
}


