package com.example.currencyconversation.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.util.Log;

import com.example.currencyconnector.CurrencyConnector;
import com.example.currencyconnector.CurrencyConnectorBuilder;
import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.models.CurrencyRate;
import com.example.currencyconversation.utils.LogSubscriberImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class VmConverter {

    private final static String TAG = "CurrencyApp";
    private final static String LOG_PREFIX = "VmConverter: ";

    private final int REFRESH_CURRENCY_TIME = 1;
    private final CurrencyConnector currencyConnector;

    private String baseCurrencyCode = "EUR";

    private final ObservableBoolean isCurrencyDataAvailable;
    private final ObservableList<CurrencyRate> currencyObservableList;
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

    public ObservableList<CurrencyRate> getLatestCurrencyList() {
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

    private void fetchLatestCurrencyVal() {
        compositeDisposable.add(currencyConnector
                .getLatestCurrencyRate(baseCurrencyCode)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(rateList -> {
                    isCurrencyDataAvailable.set(true);
                    updateCurrency(rateList.getRates());
                })
                .subscribeWith(new LogSubscriberImpl<>(TAG, LOG_PREFIX
                        + "fetchLatestCurrencyVal", true)));
    }

    private void updateCurrency(List<Currency> currencies) {
        if (currencyObservableList.isEmpty()) {
            for (int i = 0; i < currencies.size(); i++) {
                currencyObservableList.add(new CurrencyRate(currencies.get(i).getCodeName(),
                        currencies.get(i).getValue()));
            }
        }
        else {
            for (int i = 0; i < currencies.size(); i++) {
                currencyObservableList.get(i).value.set(currencies.get(i).getValue());
                currencyObservableList.get(i).currencyValue.set(String.valueOf(currencies.get(i).getValue()));
            }
        }
    }

    public void fetchAndRefreshCurrencyValueEverySecond() {
        compositeDisposable.add(Flowable.interval(REFRESH_CURRENCY_TIME, TimeUnit.SECONDS)
                .onBackpressureLatest()
                .subscribeOn(scheduler)
                .doOnNext(time -> fetchLatestCurrencyVal())
                .subscribe(
                        time -> Log.d(TAG, LOG_PREFIX + "refreshCurrencyValueEverySecond: Time: " + time),
                        error -> Log.e(TAG, LOG_PREFIX + "refreshCurrencyValueEverySecond: ", error),
                        () -> Log.d(TAG, LOG_PREFIX + "refreshCurrencyValueEverySecond Completed")
                ));
    }
}
