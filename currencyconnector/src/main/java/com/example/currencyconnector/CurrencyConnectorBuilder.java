package com.example.currencyconnector;

import com.example.currencyconnector.connector.CurrencyRetrofit;

import okhttp3.OkHttpClient;

public class CurrencyConnectorBuilder {

    public static CurrencyConnector create(OkHttpClient client) {
        return new CurrencyRetrofit(client);
    }
}
