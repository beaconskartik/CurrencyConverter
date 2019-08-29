package com.example.currencyconversation.views;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentConverterItemBinding;
import com.example.currencyconversation.models.CurrencyRate;
import com.example.currencyconversation.utils.LogSubscriberImpl;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CurrencyConverterAdapter extends RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder> {

    private final static String TAG = "CurrencyApp";
    private final static String LOG_PREFIX = "CurrencyConverterAdapter: ";

    private final List<CurrencyRate> currencyRateList;
    private final IConverterListInteractionListener mListener;

    CurrencyConverterAdapter(IConverterListInteractionListener listener, List<CurrencyRate> currencyRateList) {
        this.currencyRateList = new ArrayList<>();
        this.currencyRateList.addAll(currencyRateList);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FragmentConverterItemBinding fragmentConverterItemBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_converter_item, parent, false);
        return new ViewHolder(fragmentConverterItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.fragmentConverterItemBinding.setVm(holder);
        holder.mItem = currencyRateList.get(position);

        // OnClick Listener
        holder.compositeDisposable.add(RxView.clicks(holder.fragmentConverterItemBinding.getRoot())
                .doOnNext(val -> {
                    if (mListener != null) {
                        mListener.onListItemClickListener(holder.mItem, holder.getAdapterPosition());
                    }
                })
                .subscribeWith(new LogSubscriberImpl<>(TAG, LOG_PREFIX + "onListItemClickListener", false)));

        // Edit Text Change listener
        Observable<Double> changedValueObservable =
                RxTextView.textChanges(holder.fragmentConverterItemBinding.currencyAmount)
                        .skipInitialValue()
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .filter(val -> val.length() > 0 || (val.length() == 1 && val.charAt(0) != '.'))
                        .map(CharSequence::toString)
                        .map(Double::valueOf)
                        .distinctUntilChanged()
                        .doOnNext(value -> {
                            if (mListener != null) {
                                mListener.onListItemEditTextChangeListener(holder.mItem, holder.getAdapterPosition(), value);
                            }
                        });

        // Focus change listener
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        holder.compositeDisposable.add(RxView.focusChanges(holder.fragmentConverterItemBinding.currencyAmount)
                .skipInitialValue()
                .doOnNext(hasFocus -> {
                    if (mListener != null) {
                        mListener.onListItemFocusChangeListener(holder.mItem, holder.getAdapterPosition(), hasFocus);
                    }
                })
                .doOnNext(hasFocus -> {
                    if (hasFocus) {
                        compositeDisposable.add(changedValueObservable
                                .subscribeWith(new LogSubscriberImpl<>(TAG, LOG_PREFIX
                                        + "onListItemEditTextChangeListener", false)));
                    } else {
                        compositeDisposable.clear();
                    }
                })
                .subscribeWith(new LogSubscriberImpl<>(TAG,LOG_PREFIX + "onListItemFocusChangeListener", false)));
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.compositeDisposable.clear();
    }

    @Override
    public int getItemCount() {
        return currencyRateList.size();
    }

    void updateList(List<CurrencyRate> currencies) {
        currencyRateList.clear();
        currencyRateList.addAll(currencies);
    }

    void updateItemPosition(int from, int to) {
        CurrencyRate item = currencyRateList.get(from);
        currencyRateList.remove(from);
        currencyRateList.add(to, item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentConverterItemBinding fragmentConverterItemBinding;
        private final CompositeDisposable compositeDisposable;
        public CurrencyRate mItem;

        ViewHolder(FragmentConverterItemBinding fragmentConverterItemBinding) {
            super(fragmentConverterItemBinding.getRoot());
            this.fragmentConverterItemBinding = fragmentConverterItemBinding;
            this.compositeDisposable = new CompositeDisposable();
        }
    }
}
