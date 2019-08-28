package com.example.currencyconversation.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentRatesBinding;

public class RatesFragment extends Fragment {

    public static RatesFragment getNewInstance() {
        return new RatesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentRatesBinding fragmentRatesBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_rates, container, false);
        return fragmentRatesBinding.getRoot();
    }
}
