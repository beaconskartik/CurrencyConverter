package com.example.currencyconnector.connector;

import com.example.currencyconnector.CurrencyConnector;
import com.example.currencyconnector.models.RateList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyRetrofit implements CurrencyConnector {


    private final RetrofitApi apiService;

    public CurrencyRetrofit(OkHttpClient client) {
        apiService = getApiService(client);
    }

    @Override
    public Observable<RateList> getLatestCurrencyRate(String baseCurrencyCode) {
        return apiService.getLatestCurrencyRates(baseCurrencyCode);
    }


    private RetrofitApi getApiService(OkHttpClient client) {

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory
                .createWithScheduler(Schedulers.io());

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.SERVICE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .client(client)
                .build();
        return retrofit.create(RetrofitApi.class);
    }
}
