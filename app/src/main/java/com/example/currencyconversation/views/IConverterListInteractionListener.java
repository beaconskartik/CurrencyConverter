package com.example.currencyconversation.views;

import com.example.currencyconversation.models.CurrencyRate;

public interface IConverterListInteractionListener {

    void onListItemClickListener(CurrencyRate item, int pos);

    void onListItemFocusChangeListener(CurrencyRate item, int pos, boolean hasFocus);
}
