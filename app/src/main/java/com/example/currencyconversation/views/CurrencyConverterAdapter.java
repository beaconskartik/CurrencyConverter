package com.example.currencyconversation.views;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.currencyconversation.R;
import com.example.currencyconversation.databinding.FragmentConverterItemBinding;
import com.example.currencyconversation.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConverterAdapter extends RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder> {

    private final List<CurrencyRate> mValues;
    private final IConverterListInteractionListener mListener;

    CurrencyConverterAdapter(IConverterListInteractionListener listener) {
        mValues = new ArrayList<>();
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
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(view -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemClickListener(holder.mItem, holder.getAdapterPosition());
                }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    void updateList(List<CurrencyRate> currencies) {
        mValues.clear();
        mValues.addAll(currencies);
    }

    void updateItemPosition(int from, int to) {
        CurrencyRate item = mValues.get(from);
        mValues.remove(from);
        mValues.add(to, item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final FragmentConverterItemBinding fragmentConverterItemBinding;
        public CurrencyRate mItem;

        ViewHolder(FragmentConverterItemBinding fragmentConverterItemBinding) {
            super(fragmentConverterItemBinding.getRoot());
            this.mView = fragmentConverterItemBinding.getRoot();
            this.fragmentConverterItemBinding = fragmentConverterItemBinding;
        }
    }
}
