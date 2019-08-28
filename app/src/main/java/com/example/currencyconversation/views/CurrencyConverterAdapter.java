package com.example.currencyconversation.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currencyconnector.models.Currency;
import com.example.currencyconversation.R;
import com.example.currencyconversation.utils.ResourceUtils;
import com.example.currencyconversation.views.ConverterFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CurrencyConverterAdapter extends RecyclerView.Adapter<CurrencyConverterAdapter.ViewHolder> {

    private final List<Currency> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CurrencyConverterAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.mIdView.getContext();
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getCodeName().toUpperCase());
        holder.currencyValue.setText(String.valueOf(mValues.get(position).getValue()));

        holder.mContentView.setText(ResourceUtils.getCurrencyNameResId(context,
                mValues.get(position).getCodeName().toLowerCase()));
        holder.countryFlag.setImageResource(ResourceUtils.getCurrencyFlagResId(context,
                mValues.get(position).getCodeName().toLowerCase()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateList(List<Currency> currencies) {
        mValues.clear();
        mValues.addAll(currencies);
    }

    public void updateItemPosition(int from, int to) {
        Currency item = mValues.get(from);
        mValues.add(to, item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        private final EditText currencyValue;
        private final ImageView countryFlag;
        public Currency mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            countryFlag = (ImageView) view.findViewById(R.id.icCurrencyFlag);
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            currencyValue = (EditText) view.findViewById(R.id.txtCurrencyAmount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
