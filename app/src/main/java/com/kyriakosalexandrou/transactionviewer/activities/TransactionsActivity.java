package com.kyriakosalexandrou.transactionviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.kyriakosalexandrou.transactionviewer.R;
import com.kyriakosalexandrou.transactionviewer.adapters.TransactionsAdapter;
import com.kyriakosalexandrou.transactionviewer.helpers.ProductsCreatorHelper;
import com.kyriakosalexandrou.transactionviewer.models.Product;

/**
 * Created by Kyriakos on 10/01/2016.
 */
public class TransactionsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra(ProductsCreatorHelper.EXTRA_PRODUCT);

        TextView totalAmount = (TextView) findViewById(R.id.totalAmount);
        totalAmount.setText("Total: £" + product.getTotalAmountInGBP() + "");

        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(this, product.getTransactions());
        ListView transactionsList = (ListView) findViewById(R.id.transactionsList);
        transactionsList.setAdapter(transactionsAdapter);
    }
}
