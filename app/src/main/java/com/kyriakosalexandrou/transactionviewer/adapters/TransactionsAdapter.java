package com.kyriakosalexandrou.transactionviewer.adapters;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kyriakosalexandrou.transactionviewer.R;
import com.kyriakosalexandrou.transactionviewer.models.Product;
import com.kyriakosalexandrou.transactionviewer.models.Transaction;

import java.util.ArrayList;

public class TransactionsAdapter extends BaseAdapter {
    private ArrayList<Transaction> mTransactions;
    private Context mContext;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        mContext = context;
        mTransactions = transactions;
    }

    @Override
    public int getCount() {
        return mTransactions.size();
    }

    @Override
    public Transaction getItem(int position) {
        return mTransactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_transaction_list, null);
        }

        TextView originalCurrency = (TextView) convertView.findViewById(R.id.transaction_original_currency);
        TextView originalValue = (TextView) convertView.findViewById(R.id.transaction_original_value);
        TextView gbpValue = (TextView) convertView.findViewById(R.id.transaction_GBP_value);


        originalCurrency.setText(mTransactions.get(position).getCurrency());
        originalValue.setText(" " + mTransactions.get(position).getAmount());
        gbpValue.setText(" " + mTransactions.get(position).getAmountInGBP());

        return convertView;
    }
}
