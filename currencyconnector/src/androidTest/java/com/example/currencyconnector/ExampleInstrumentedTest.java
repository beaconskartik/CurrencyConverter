package com.example.currencyconnector;

import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import com.example.currencyconnector.models.RateList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private final String TAG = "NewsEntityDataConnectorAndroidTest:";
    private CurrencyConnector client;

    @Before
    public void setUp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        client = CurrencyConnectorBuilder.create(okHttpClient);
    }

    @Test
    public void fetchLatestRateList() {
        TestObserver<RateList> testObserver = client
                .getLatestCurrencyRate("EUR")
                .subscribeOn(Schedulers.io())
                .test();

        testObserver.awaitTerminalEvent();

        testObserver
                .assertNoErrors()
                .assertSubscribed()
                .assertValue(val -> TextUtils.equals(val.getBase(), "EUR"));

        testObserver.dispose();
    }
}
