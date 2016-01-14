package com.kyriakosalexandrou.transactionviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kyriakosalexandrou.transactionviewer.R;
import com.kyriakosalexandrou.transactionviewer.Util;
import com.kyriakosalexandrou.transactionviewer.adapters.ProductsAdapter;
import com.kyriakosalexandrou.transactionviewer.models.Product;
import com.kyriakosalexandrou.transactionviewer.models.Rate;
import com.kyriakosalexandrou.transactionviewer.models.Transaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_PRODUCT = "PRODUCT";
    private ListView mProductsListView;
    private String FINAL_CURRENCY = "GBP";

    private ArrayList<Rate> mRates;
    private ArrayList<Transaction> mTransactions;
    private ArrayList<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        mRates = createRates(gson);
        mTransactions = createTransactions(gson);
        mProducts = createProducts(mTransactions);

        setAdapters();
        setListeners();

        for (Product product : mProducts) {
            calculateProductsTransactionsTotalAmount(product);
        }
    }

    private void bindViews() {
        mProductsListView = (ListView) findViewById(R.id.productsList);
    }

    private void setAdapters() {
        ProductsAdapter productsAdapter = new ProductsAdapter(this, mProducts);
        mProductsListView.setAdapter(productsAdapter);
    }

    private void setListeners() {
        mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TransactionsActivity.class);
                intent.putExtra(EXTRA_PRODUCT, mProducts.get(position));
                startActivity(intent);
            }
        });
    }

    private ArrayList<Transaction> createTransactions(Gson gson) {
        String transactionData = Util.loadJSONFromAsset(this, "transactions.json");
        return createTransactionModels(transactionData, gson);
    }

    @Nullable
    private ArrayList<Transaction> createTransactionModels(String rateData, Gson gson) {
        Type collectionType = new TypeToken<Collection<Transaction>>() {
        }.getType();
        return gson.fromJson(rateData, collectionType);
    }

    private ArrayList<Rate> createRates(Gson gson) {
        String rateData = Util.loadJSONFromAsset(this, "rates.json");
        return createRateModels(rateData, gson);
    }

    @Nullable
    private ArrayList<Rate> createRateModels(String rateData, Gson gson) {
        Type collectionType = new TypeToken<Collection<Rate>>() {
        }.getType();
        return gson.fromJson(rateData, collectionType);
    }

    private ArrayList<Product> createProducts(ArrayList<Transaction> transactions) {
        ArrayList<Product> products = new ArrayList<>();
        boolean wasProductUpdated;

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            wasProductUpdated = addTransactionInRespectiveProduct(products, transaction);

            if (!wasProductUpdated) {
                products.add(new Product(transaction.getSku(), transaction));
            }
        }
        return products;
    }


    /**
     * searches through the products that already exist and adds the transaction to the correct product
     *
     * @param transaction the transaction to add to it's respective product if it already exists
     * @return
     */
    private boolean addTransactionInRespectiveProduct(ArrayList<Product> products, Transaction transaction) {
        boolean wasProductUpdated = false;

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            if (product.getSKU().equals(transaction.getSku())) {
                //a product already exists with this SKU, lets add this transaction to this product
                product.addTransaction(transaction);
                wasProductUpdated = true;
                break;
            }
        }
        return wasProductUpdated;
    }

    private void calculateProductsTransactionsTotalAmount(Product product) {
        float amount = 0;
        String currentTransactionCurrency;
        float currentTransactionAmount;

        for (Transaction transaction : product.getTransactions()) {
            currentTransactionAmount = Float.parseFloat(transaction.getAmount());
            currentTransactionCurrency = transaction.getCurrency();

            amount = findFromSomethingToGBP(
                    transaction,
                    currentTransactionAmount,
                    currentTransactionCurrency,
                    currentTransactionCurrency
            );
            product.addToTotalAmount(amount);
        }

        Log.v("kiki", "**********************");
        Log.v("kiki", "Product SKU: " + product.getSKU());
        Log.v("kiki", "Total transactions: " + product.getTotalTransactions());
        Log.v("kiki", "Total transactions amount in " + FINAL_CURRENCY + ": " + product.getTotalAmountInGBP());
        Log.v("kiki", "**********************");
    }

    private float findFromSomethingToGBP(Transaction currentTransaction, Float amount,
                                        String currentTransacionCurrency, String startCurrency) {
        boolean foundFromSomethingToGBP = false;
        Rate rate;

        for (int i = 0; i < mRates.size(); i++) {
            rate = mRates.get(i);

            // check if the current currency has a rate conversion to our FINAL_CURRENCY
            if (currentTransacionCurrency.equals(rate.getFrom())
                    && FINAL_CURRENCY.equals(rate.getTo())) {
                amount *= Float.parseFloat(rate.getRate());
                currentTransaction.setAmountInGBP(amount);
                foundFromSomethingToGBP = true;
            }
        }
        if(!foundFromSomethingToGBP){
            findFromSomethingToSomething(
                    currentTransaction, amount,
                    currentTransacionCurrency, startCurrency);
        }

        return amount;
    }

    private void findFromSomethingToSomething(Transaction currentTransaction, Float amount,
                                              String currentTransacionCurrency, String startCurrency) {
        Rate rate;

        for (int i = 0; i < mRates.size(); i++) {
            rate = mRates.get(i);

            //found from our current currency to something else other than our startCurrency
            if (rate.getFrom().equals(currentTransacionCurrency)
                    && !rate.getTo().equals(startCurrency)) {
                amount *= Float.parseFloat(rate.getRate());
                //check again if this rate currency has a conversion to our FINAL_CURRENCY
                findFromSomethingToGBP(currentTransaction, amount, rate.getTo(), startCurrency);
                break;
            }
        }
    }


}