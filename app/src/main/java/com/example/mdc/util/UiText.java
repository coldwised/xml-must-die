package com.example.mdc.util;

import android.content.Context;

import androidx.annotation.StringRes;

public interface UiText {
    String asString (Context context);

    class StringResource implements UiText {
        @StringRes
        final int resId;

        public StringResource(int resId) {
            this.resId = resId;
        }

        @Override
        public String asString(Context context) {
            return null;
        }
    }

    class DynamicString implements UiText {

        final String value;

        public DynamicString(String value) {
            this.value = value;
        }

        @Override
        public String asString(Context context) {
            return value;
        }
    }
}
