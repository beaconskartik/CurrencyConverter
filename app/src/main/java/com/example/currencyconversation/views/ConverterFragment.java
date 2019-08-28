package com.example.currencyconversation.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentConverterBinding;
import com.example.currencyconversation.viewModels.VmConverter;
import com.example.currencyconversation.viewModels.VmLocator;

public class ConverterFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CurrencyConverterAdapter adapter;
    private VmConverter currencyConverter;

    public ConverterFragment() {
    }

    public static ConverterFragment getNewInstance() {
        return new ConverterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyConverter = VmLocator.getInstance().getVmConverter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentConverterBinding converterBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_converter, container, false);
        converterBinding.setVm(currencyConverter);

        recyclerView = converterBinding.list;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CurrencyConverterAdapter(mListener);
        recyclerView.setAdapter(adapter);
        setUpObservableListChangeListener();
        return converterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currencyConverter.fetchLatestCurrencyVal();
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

    private void setUpObservableListChangeListener() {
        currencyConverter.getLatestCurrencyList()
                .addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Currency>>() {
                    @Override
                    public void onChanged(ObservableList<Currency> sender) {
                        adapter.updateList(sender);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemRangeChanged(ObservableList<Currency> sender, int positionStart, int itemCount) {
                        adapter.updateList(sender);
                        adapter.notifyItemRangeChanged(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeInserted(ObservableList<Currency> sender, int positionStart, int itemCount) {
                        adapter.updateList(sender);
                        adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList<Currency> sender, int fromPosition, int toPosition, int itemCount) {
                        adapter.updateList(sender);
                        adapter.notifyItemMoved(fromPosition, toPosition);
                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList<Currency> sender, int positionStart, int itemCount) {
                        adapter.updateList(sender);
                        adapter.notifyItemRangeRemoved(positionStart, itemCount);
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public CurrencyConverterAdapter getAdapter() {
        return adapter;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Currency item, int pos);
    }
}
