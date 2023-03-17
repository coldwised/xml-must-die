package com.example.mdc.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

public interface UiText {
    String asString (@NotNull Context context);

    class StringResource implements UiText {

        @StringRes
        final int resId;

        public StringResource(int resId) {
            this.resId = resId;
        }

        @NotNull
        @Override
        public String asString(@NonNull Context context) {
            return context.getString(resId);
        }
    }

    class DynamicString implements UiText {

        @NotNull
        final String value;

        public DynamicString(@NonNull String value) {
            this.value = value;
        }

        @NotNull
        @Override
        public String asString(@NonNull Context context) {
            return value;
        }
    }
}
