package com.example.currencyconnector.connector;

import com.example.currencyconnector.models.RateList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {

    String SERVICE_ENDPOINT = "https://revolut.duckdns.org";

    @GET("latest")
    Observable<RateList> getLatestCurrencyRates(@Query("base") String baseCurrencyCode);
}
