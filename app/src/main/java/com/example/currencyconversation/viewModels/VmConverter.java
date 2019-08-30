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

import java.util.List;
import java.util.Locale;
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
    private final static String LOG_PREFIX = "VmConverter:";

    private final static int REFRESH_CURRENCY_TIME = 1;
    private final static String BASE_CURRENCY_CODE = "EUR";

    private final CurrencyConnector currencyConnector;

    private final ObservableBoolean isCurrencyDataAvailable;
    private final ObservableList<CurrencyRate> currencyObservableList;
    private final BehaviorSubject<Pair<String, Double>> currencyAmountChanged
            = BehaviorSubject.createDefault(Pair.create(BASE_CURRENCY_CODE, 1.0));
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

    public void updateBaseOrChangedAmount(String newBaseCurrencyCode, double changedAmount) {
        if (!TextUtils.equals(currencyAmountChanged.getValue().first, newBaseCurrencyCode)
                || (TextUtils.equals(currencyAmountChanged.getValue().first, newBaseCurrencyCode)
                && currencyAmountChanged.getValue().second != changedAmount)) {
            Log.d(TAG, LOG_PREFIX + "updateBaseOrChangedAmount: newBaseCurrencyCode: " + newBaseCurrencyCode + " changeAmount: " + changedAmount);
            currencyAmountChanged.onNext(Pair.create(newBaseCurrencyCode, changedAmount));
        }
    }

    public void updateBaseAmount(String currencyCodeName) {
        if (TextUtils.equals(currencyCodeName, currencyAmountChanged.getValue().first)) {
            updateCurrencyList(currencyCodeName, currencyAmountChanged.getValue().second, 1.0);
        } else {
            Log.e(TAG, LOG_PREFIX + "error something is not right, may be focus logic?");
        }
    }

    private Observable<Double> fetchLatestCurrencyVal(final String baseCurrencyCode, final double amountChanged) {
        return currencyConnector
                .getLatestCurrencyRate(baseCurrencyCode)
                .subscribeOn(scheduler)
                .doOnNext(rateList -> isCurrencyDataAvailable.set(true))
                .flatMap(rateList -> updateCurrency(rateList.getRates(), baseCurrencyCode, amountChanged));
    }

    private Observable<Double> updateCurrency(final List<Currency> currencies,
                                              final String currencyCode,
                                              final double amountChanged) {
        return Observable.defer(() -> {
            if (currencyObservableList.isEmpty()) {
                return Observable.fromIterable(currencies)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(currency -> currencyObservableList.add(new CurrencyRate(currency.getCodeName(),
                                currency.getValue() * amountChanged)))
                        .map(currency -> amountChanged);
            }
            else {
                Observable<Pair<Integer, Currency>> currencyUpdateObservable =
                        Observable.range(0, currencies.size())
                                .zipWith(currencies, Pair::new)
                                .filter(integerCurrencyPair ->
                                        !TextUtils.equals(integerCurrencyPair.second.getCodeName(),
                                                currencyCode));

                return currencyUpdateObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(currencyPair -> {
                            updateCurrencyList(currencyPair.second.getCodeName(), currencyPair.second.getValue(), amountChanged);
                        })
                        .map(integerCurrencyPair -> amountChanged);

            }});
    }

    private void updateCurrencyList(String currencyCode, double value, double changedValue) {
        for (CurrencyRate currencyRate : currencyObservableList) {
            if (TextUtils.equals(currencyRate.codeName, currencyCode)) {
                currencyRate.value.set(value * changedValue);
                currencyRate.currencyValue.set(String.format(Locale.getDefault(),"%.3f", value * changedValue));
                break;
            }
        }
    }

    public void fetchAndRefreshCurrencyValueEverySecond() {
        compositeDisposable.add(Observable
                .combineLatest(Flowable.interval(REFRESH_CURRENCY_TIME, TimeUnit.SECONDS, scheduler)
                                .onBackpressureLatest().toObservable(), currencyAmountChanged.distinctUntilChanged(),
                        (time, currencyCodeDoublePair) -> currencyCodeDoublePair)
                .flatMap(currencyRateDoublePair -> fetchLatestCurrencyVal(currencyRateDoublePair.first,
                        currencyRateDoublePair.second))
                .onErrorReturn(val -> -1.0)
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
