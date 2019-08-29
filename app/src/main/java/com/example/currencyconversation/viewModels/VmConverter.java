package com.example.currencyconversation.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

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
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.OkHttpClient;

public class VmConverter {

    private final static String TAG = "CurrencyApp";
    private final static String LOG_PREFIX = "VmConverter: ";
    private final static int REFRESH_CURRENCY_TIME = 1;

    private final CurrencyConnector currencyConnector;

    private final String baseCurrencyCode = "EUR";
    private final List<Currency> latestCurrencyRateList;

    private final ObservableBoolean isCurrencyDataAvailable;
    private final ObservableList<CurrencyRate> currencyObservableList;

    private final BehaviorSubject<Pair<CurrencyRate, Boolean>> startRefreshingCurrencyValue = BehaviorSubject.create();
    private final BehaviorSubject<Pair<CurrencyRate, Double>> currencyAmountChanged = BehaviorSubject
            .createDefault(Pair.create(new CurrencyRate(baseCurrencyCode, 1.0), 1.0));
    private final Scheduler scheduler;
    private final CompositeDisposable compositeDisposable;

    VmConverter() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        currencyConnector = CurrencyConnectorBuilder.create(okHttpClient);
        isCurrencyDataAvailable = new ObservableBoolean(false);
        currencyObservableList = new ObservableArrayList<>();
        latestCurrencyRateList = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();

        scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    }

    public ObservableList<CurrencyRate> getLatestCurrencyList() {
        return currencyObservableList;
    }

    public ObservableBoolean IsCurrencyDataAvailable() {
        return isCurrencyDataAvailable;
    }

    public void canStartRefreshingCurrencyValue(CurrencyRate currency, Boolean canStart) {
        startRefreshingCurrencyValue.onNext(Pair.create(currency, canStart));
    }

    public void updateChangedAmount(CurrencyRate currency, double changedAmount) {
        currencyAmountChanged.onNext(Pair.create(currency, changedAmount));
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
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
        latestCurrencyRateList.clear();
        latestCurrencyRateList.addAll(currencies);
        if (currencyObservableList.isEmpty()) {
            for (int i = 0; i < currencies.size(); i++) {
                currencyObservableList.add(new CurrencyRate(currencies.get(i).getCodeName(),
                        currencies.get(i).getValue()));
            }
        }
        else {
            Observable<Pair<Integer, Currency>> currencyUpdateObservable =
                    Observable.range(0, currencies.size())
                            .zipWith(currencies, Pair::new)
                            .flatMap(currency -> startRefreshingCurrencyValue, Pair::new)
                            .filter(currencyPairPair -> (!TextUtils.equals(currencyPairPair.first.second.getCodeName(),
                                    currencyPairPair.second.first.codeName) || (currencyPairPair.second.second && TextUtils.equals(currencyPairPair.first.second.getCodeName(),
                                    currencyPairPair.second.first.codeName))))
                            .map(currencyPairPair -> currencyPairPair.first);

//            compositeDisposable.add(Observable.combineLatest(currencyUpdateObservable, currencyAmountChanged, (integerCurrencyPair, currencyRateDoublePair) -> {
//                double val = (integerCurrencyPair.second.getValue()/currencyRateDoublePair.first.value.get()) * currencyRateDoublePair.second;
//                return Pair.create(integerCurrencyPair.first, val);
//            })
            compositeDisposable.add(currencyUpdateObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(currencyPair -> {
                        currencyObservableList.get(currencyPair.first).value.set(currencyPair.second.getValue());
                        currencyObservableList.get(currencyPair.first).currencyValue
                                .set(String.valueOf(currencyPair.second.getValue()));
                    })
                    .subscribeWith(new LogSubscriberImpl<>(TAG, LOG_PREFIX + "updateCurrency", true)));
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

    public void cleanUp() {
        compositeDisposable.clear();
    }
}
