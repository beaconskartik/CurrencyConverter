package com.example.currencyconversation;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.views.AlertsFragment;
import com.example.currencyconversation.views.ConverterFragment;
import com.example.currencyconversation.views.RatesFragment;

public class CurrencyActivity extends AppCompatActivity {

    private static final String FRAGMENT_CONVERTER = "fragment_currency_converter";
    private static final String FRAGMENT_ALL_RATES = "fragment_all_rates";
    private static final String FRAGMENT_ALERTS = "fragment_alerts";

    private ConverterFragment converterFragment;
    private RatesFragment ratesFragment;
    private AlertsFragment alertsFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_all_rates:
                    replaceFragment(ratesFragment, R.id.phone_container, FRAGMENT_ALL_RATES);
                    return true;
                case R.id.navigation_converter:
                    replaceFragment(converterFragment, R.id.phone_container, FRAGMENT_CONVERTER);
                    return true;
                case R.id.navigation_alerts:
                    replaceFragment(alertsFragment, R.id.phone_container, FRAGMENT_ALERTS);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ratesFragment = RatesFragment.getNewInstance();
        converterFragment = ConverterFragment.getNewInstance();
        alertsFragment = AlertsFragment.getNewInstance();

        navigation.setSelectedItemId(R.id.navigation_converter);
        replaceFragment(converterFragment, R.id.phone_container, FRAGMENT_CONVERTER);
    }

    private void addFragment(Fragment fragment, @IdRes int resourceId, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(resourceId, fragment, tag)
                .addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, @IdRes int resourceId, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(resourceId, fragment, tag);
        fragmentTransaction.commit();
    }
}
