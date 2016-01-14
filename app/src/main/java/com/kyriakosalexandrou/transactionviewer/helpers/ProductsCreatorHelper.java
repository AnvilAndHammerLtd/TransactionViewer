package com.kyriakosalexandrou.transactionviewer.helpers;

import android.util.Log;

import com.kyriakosalexandrou.transactionviewer.models.Product;
import com.kyriakosalexandrou.transactionviewer.models.Rate;
import com.kyriakosalexandrou.transactionviewer.models.Transaction;

import java.util.ArrayList;

/**
 * Created by Kyriakos on 14/01/2016.
 */
public class ProductsCreatorHelper {
    public static String EXTRA_PRODUCT = "PRODUCT";
    private final static String TAG = ProductsCreatorHelper.class.getSimpleName();
    private ArrayList<Rate> mRates;
    private String mCurrencyToConvertTransactionsTo;

    public ProductsCreatorHelper(ArrayList<Rate> rates, String currencyToConvertTransactionsTo) {
        mRates = rates;
        mCurrencyToConvertTransactionsTo = currencyToConvertTransactionsTo;
    }

    public ArrayList<Product> createProducts(ArrayList<Transaction> transactions) {
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

    public void calculateProductsTransactionsTotalAmount(Product product) {
        float amount;
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

        Log.v(TAG, "**********************");
        Log.v(TAG, "Product SKU: " + product.getSKU());
        Log.v(TAG, "Total transactions: " + product.getTotalTransactions());
        Log.v(TAG, "Total transactions amount in " + mCurrencyToConvertTransactionsTo + ": " + product.getTotalAmountInGBP());
        Log.v(TAG, "**********************");
    }

    private float findFromSomethingToGBP(Transaction currentTransaction, Float amount,
                                         String currentTransacionCurrency, String startCurrency) {
        boolean foundFromSomethingToGBP = false;
        Rate rate;

        for (int i = 0; i < mRates.size(); i++) {
            rate = mRates.get(i);

            // check if the current currency has a rate conversion to our FINAL_CURRENCY
            if (currentTransacionCurrency.equals(rate.getFrom())
                    && mCurrencyToConvertTransactionsTo.equals(rate.getTo())) {
                amount *= Float.parseFloat(rate.getRate());
                currentTransaction.setAmountInGBP(amount);
                foundFromSomethingToGBP = true;
            }
        }

        if (!foundFromSomethingToGBP) {
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