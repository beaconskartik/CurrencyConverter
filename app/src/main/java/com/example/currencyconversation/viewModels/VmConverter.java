package com.example.currencyconversation.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.example.currencyconnector.CurrencyConnector;
import com.example.currencyconnector.CurrencyConnectorBuilder;
import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.utils.LogSubscriberImpl;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class VmConverter {

    private final static String TAG = "CurrencyApp";
    private final static String LOG_PREFIX = "VmConverter: ";
    private final CurrencyConnector currencyConnector;

    private String baseCurrencyCode = "EUR";

    private final ObservableBoolean isCurrencyDataAvailable;
    private final ObservableList<Currency> currencyObservableList;
    private final Scheduler scheduler;
    private final CompositeDisposable compositeDisposable;

    VmConverter() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        currencyConnector = CurrencyConnectorBuilder.create(okHttpClient);
        isCurrencyDataAvailable = new ObservableBoolean(false);
        currencyObservableList = new ObservableArrayList<>();
        compositeDisposable = new CompositeDisposable();

        scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    }

    public ObservableList<Currency> getLatestCurrencyList() {
        return currencyObservableList;
    }

    public ObservableBoolean IsCurrencyDataAvailable() {
        return isCurrencyDataAvailable;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public void fetchLatestCurrencyVal() {
        compositeDisposable.add(currencyConnector
                .getLatestCurrencyRate(baseCurrencyCode)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(rateList -> {
                    isCurrencyDataAvailable.set(true);
                    currencyObservableList.addAll(rateList.getRates());
                })
                .subscribeWith(new LogSubscriberImpl<>(TAG, LOG_PREFIX
                        + "fetchLatestCurrencyVal", true)));
    }
}
