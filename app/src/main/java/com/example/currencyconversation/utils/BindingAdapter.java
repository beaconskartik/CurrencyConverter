package com.example.currencyconversation.utils;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

public final class BindingAdapter {

    @android.databinding.BindingAdapter({"countryFlag"})
    public static void setCallAvatarDrawable(final ImageView imageView, @DrawableRes final int resId) {
        imageView.setImageResource(resId);
    }
}
