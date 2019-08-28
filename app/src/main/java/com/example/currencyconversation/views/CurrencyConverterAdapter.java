package com.example.currencyconversation.views;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentConverterItemBinding;
import com.example.currencyconversation.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

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

        holder.fragmentConverterItemBinding
                .getRoot()
                .setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onListItemClickListener(holder.mItem, holder.getAdapterPosition());
                    }
                });

        holder.fragmentConverterItemBinding
                .currencyAmount
                .setOnFocusChangeListener((v, hasFocus) -> {
                    if (mListener != null) {
                        mListener.onListItemFocusChangeListener(holder.mItem, holder.getAdapterPosition(), hasFocus);
                    }
                });
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
