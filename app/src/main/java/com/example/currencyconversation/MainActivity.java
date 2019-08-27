package com.example.currencyconversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.views.CurrencyConverterFragment;

public class MainActivity extends AppCompatActivity implements CurrencyConverterFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;
    private CurrencyConverterFragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    // mTextMessage.setText(R.string.title_dashboard);
                    addFragment(fragment, R.id.phone_container, "currency_converter");
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = CurrencyConverterFragment.newInstance();
    }

    private void addFragment(Fragment fragment, int resourceId, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(resourceId, fragment, tag)
                .addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Currency item, int pos) {
        Log.d("kartik", "onListFragmentInteraction: item: " + item.getCodeName());

        if (pos != 0) {
            fragment.getAdapter().notifyItemMoved(pos, 0);
            fragment.getAdapter().updateItemPosition(pos, 0);
        }
    }
}
