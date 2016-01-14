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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ProductsAdapter extends BaseAdapter {
    private ArrayList<Product> mProducts;
    private Context mContext;
    private TextView mProductName;
    private TextView mProductNumberOfTransaction;

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        mContext = context;
        mProducts = products;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Product getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_product_list, null);
        }

        mProductName = (TextView) convertView.findViewById(R.id.product_name);
        mProductNumberOfTransaction = (TextView) convertView.findViewById(R.id.product_number_of_transactions);

        mProductName.setText(mProducts.get(position).getSKU());
        mProductNumberOfTransaction.setText(String.valueOf(mProducts.get(position).getTotalTransactions()));

        return convertView;
    }
}
