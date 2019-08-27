package com.example.currencyconversation.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.R;
import com.example.currencyconversation.viewModels.VmCurrencyConverter;
import com.example.currencyconversation.viewModels.VmLocator;

public class CurrencyConverterFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CurrencyConverterAdapter adapter;
    private VmCurrencyConverter currencyConverter;

    public CurrencyConverterFragment() {
    }

    public static CurrencyConverterFragment newInstance() {
        CurrencyConverterFragment fragment = new CurrencyConverterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyConverter = VmLocator.getInstance().getVmCurrencyConverter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new CurrencyConverterAdapter(mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currencyConverter
                .fetchLatestCurrencyVal("EUR")
                .doOnNext(rateList -> {
                    adapter.updateList(rateList.getRates());
                })
                .subscribe();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public CurrencyConverterAdapter getAdapter() {
        return adapter;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Currency item, int pos);
    }
}
