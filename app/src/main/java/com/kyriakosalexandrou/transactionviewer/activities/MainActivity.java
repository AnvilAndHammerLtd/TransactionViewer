package com.kyriakosalexandrou.transactionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kyriakosalexandrou.transactionviewer.JsonUtil;
import com.kyriakosalexandrou.transactionviewer.R;
import com.kyriakosalexandrou.transactionviewer.adapters.ProductsAdapter;
import com.kyriakosalexandrou.transactionviewer.helpers.ProductsCreatorHelper;
import com.kyriakosalexandrou.transactionviewer.interfaces.CommonActivityUiLogicHelper;
import com.kyriakosalexandrou.transactionviewer.models.Product;
import com.kyriakosalexandrou.transactionviewer.models.Rate;
import com.kyriakosalexandrou.transactionviewer.models.Transaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CommonActivityUiLogicHelper {
    private final static String TAG = MainActivity.class.getSimpleName();

    private ListView mProductsListView;
    public final static String CURRENCY_TO_CONVERT_TRANSACTIONS_TO = "GBP";

    private ArrayList<Rate> mRates;
    private ArrayList<Transaction> mTransactions;
    private ArrayList<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        mRates = JsonUtil.createRates(this);
        mTransactions = JsonUtil.createTransactions(this);

        ProductsCreatorHelper productsCreatorHelper = new ProductsCreatorHelper(mRates);
        mProducts = productsCreatorHelper.createProducts(mTransactions);
        float productTransactionsTotalAmount;

        for (Product product : mProducts) {
            productTransactionsTotalAmount = productsCreatorHelper.calculateProductTransactionsTotalAmount(product.getTransactions(), CURRENCY_TO_CONVERT_TRANSACTIONS_TO);
            product.setTotalAmount(productTransactionsTotalAmount);

            Log.v(TAG, "**********************");
            Log.v(TAG, "Product SKU: " + product.getSKU());
            Log.v(TAG, "Total transactions: " + product.getTotalTransactions());
            Log.v(TAG, "Total transactions amount in " + CURRENCY_TO_CONVERT_TRANSACTIONS_TO + ": " + product.getTotalAmount());
            Log.v(TAG, "**********************");
        }

        setAdapters();
        setListeners();
    }

    @Override
    public void bindViews() {
        mProductsListView = (ListView) findViewById(R.id.productsList);
    }

    @Override
    public void setAdapters() {
        ProductsAdapter productsAdapter = new ProductsAdapter(this, mProducts);
        mProductsListView.setAdapter(productsAdapter);
    }

    @Override
    public void setListeners() {
        mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TransactionsActivity.class);
                intent.putExtra(ProductsCreatorHelper.EXTRA_PRODUCT, mProducts.get(position));
                startActivity(intent);
            }
        });
    }
}