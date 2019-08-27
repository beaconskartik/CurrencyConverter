package com.example.currencyconversation.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public final class ResourceUtils {

    @StringRes public static int getCurrencyNameResId(Context context, String currencySymbol) {
        return context.getResources().getIdentifier("currency_" + currencySymbol + "_name", "string",
                context.getPackageName());
    }

    @DrawableRes public static int getCurrencyFlagResId(Context context, String currencySymbol) {
        return context.getResources().getIdentifier("ic_" + currencySymbol + "_flag", "mipmap",
                context.getPackageName());
    }
}
