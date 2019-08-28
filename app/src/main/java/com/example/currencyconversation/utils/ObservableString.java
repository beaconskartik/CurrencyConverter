package com.example.currencyconversation.utils;

import android.databinding.ObservableField;
import android.text.TextUtils;

public class ObservableString extends ObservableField<String> {

    @Override
    public void set(String value) {
        // generics observable fields are using != for comparison, won't work with strings...
        if (TextUtils.isEmpty(value)) {
            super.set(value);
        } else if (!value.equals(this.get())) {
            super.set(value);
        }
    }
}