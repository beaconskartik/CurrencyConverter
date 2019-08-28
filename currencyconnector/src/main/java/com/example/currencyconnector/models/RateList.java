package com.example.currencyconnector.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RateList {

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("rates")
    @Expose
    private Map<String, Double> rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public List<Currency> getRates() {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency(base, 1.00));
        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            currencyList.add(new Currency(rate.getKey(), rate.getValue()));
        }
        return currencyList;
    }
}
