package com.example.currencyconnector;

import com.example.currencyconnector.models.RateList;

import io.reactivex.Observable;

public interface CurrencyConnector {

    Observable<RateList> getLatestCurrencyRate(String baseCurrencyCode);
}
