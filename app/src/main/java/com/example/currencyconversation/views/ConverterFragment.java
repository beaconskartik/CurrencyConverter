package com.example.currencyconversation.views;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentConverterBinding;
import com.example.currencyconversation.models.CurrencyRate;
import com.example.currencyconversation.viewModels.VmConverter;
import com.example.currencyconversation.viewModels.VmLocator;

public class ConverterFragment extends Fragment {

    private final static String TAG = "CurrencyApp";
    private final static String LOG_PREFIX = "ConverterFragment: ";

    private IConverterListInteractionListener converterListInteractionListener;
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
        converterListInteractionListener = getConverterListInteractionListener();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentConverterBinding converterBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_converter, container, false);
        converterBinding.setVm(currencyConverter);

        recyclerView = converterBinding.list;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CurrencyConverterAdapter(converterListInteractionListener);
        recyclerView.setAdapter(adapter);
        setUpObservableListChangeListener();
        return converterBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currencyConverter.fetchAndRefreshCurrencyValueEverySecond();
    }

    private void setUpObservableListChangeListener() {
        currencyConverter.getLatestCurrencyList()
                .addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<CurrencyRate>>() {
                    @Override
                    public void onChanged(ObservableList<CurrencyRate> sender) {
                        Log.d(TAG, LOG_PREFIX + "onChanged");
                        adapter.updateList(sender);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemRangeChanged(ObservableList<CurrencyRate> sender,
                                                   int positionStart, int itemCount) {
                        Log.d(TAG, LOG_PREFIX + "onItemRangeChanged");
                        adapter.updateList(sender);
                        adapter.notifyItemRangeChanged(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeInserted(ObservableList<CurrencyRate> sender,
                                                    int positionStart, int itemCount) {
                        Log.d(TAG, LOG_PREFIX + "onItemRangeInserted");
                        adapter.updateList(sender);
                        adapter.notifyItemRangeInserted(positionStart, itemCount);
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList<CurrencyRate> sender,
                                                 int fromPosition, int toPosition, int itemCount) {
                        Log.d(TAG, LOG_PREFIX + "onItemRangeMoved");
                        adapter.updateList(sender);
                        adapter.notifyItemMoved(fromPosition, toPosition);
                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList<CurrencyRate> sender,
                                                   int positionStart, int itemCount) {
                        Log.d(TAG, LOG_PREFIX + "onItemRangeRemoved");
                        adapter.updateList(sender);
                        adapter.notifyItemRangeRemoved(positionStart, itemCount);
                    }
                });
    }

    private IConverterListInteractionListener getConverterListInteractionListener() {
        return new IConverterListInteractionListener() {
            @Override
            public void onListItemClickListener(CurrencyRate item, int pos) {
                Log.d(TAG, LOG_PREFIX + " item: " + item.codeName + " pos: " + pos);
                if (pos != 0) {
                    adapter.notifyItemRemoved(pos);
                    adapter.updateItemPosition(pos, 0);
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                }
            }
        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        converterListInteractionListener = null;
    }
}
