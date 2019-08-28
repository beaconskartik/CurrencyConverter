package com.example.currencyconversation.models;

import android.databinding.ObservableField;

import com.example.currencyconversation.utils.ObservableString;

public class CurrencyRate {

    public String codeName;
    public ObservableField<Double> value = new ObservableField<>();
    public ObservableString currencyValue = new ObservableString();

    public CurrencyRate(String codeName, Double value) {
        this.codeName = codeName;
        this.value.set(value);
        this.currencyValue.set(String.valueOf(value));
    }
}
