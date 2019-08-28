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
import com.example.currencyconversation.databinding.FragmentAlertsBinding;

public class AlertsFragment extends Fragment {

    public static AlertsFragment getNewInstance() {
        return new AlertsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentAlertsBinding fragmentAlertsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_alerts, container, false);
        return fragmentAlertsBinding.getRoot();
    }
}
