package com.example.currencyconversation.viewModels;

import com.example.currencyconnector.CurrencyConnector;
import com.example.currencyconnector.CurrencyConnectorBuilder;
import com.example.currencyconnector.models.RateList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.OkHttpClient;

public class VmCurrencyConverter {

    private CompositeDisposable compositeDisposable;
    private CurrencyConnector currencyConnector;

    VmCurrencyConverter() {
       compositeDisposable = new CompositeDisposable();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
       currencyConnector = CurrencyConnectorBuilder.create(okHttpClient);
    }

    public Observable<RateList> fetchLatestCurrencyVal(String currencySymbol) {
        return currencyConnector
                .getLatestCurrencyRate("EUR")
                .observeOn(AndroidSchedulers.mainThread());
    }
}
