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

    public ProductsCreatorHelper(ArrayList<Rate> rates) {
        mRates = rates;
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

    public float calculateProductTransactionsTotalAmount(ArrayList<Transaction> transactions, String currencyToConvertAllTransactionsTo) {
        float amount = 0;
        String currentTransactionCurrency;
        float currentTransactionAmount;

        for (int i=0; i<transactions.size(); i++) {
            Transaction transaction = transactions.get(i);

            currentTransactionAmount = Float.parseFloat(transaction.getAmount());
            currentTransactionCurrency = transaction.getCurrency();

            findFromSomethingToTheEndCurrency(
                    transaction,
                    currentTransactionAmount,
                    currentTransactionCurrency,
                    currentTransactionCurrency,
                    currencyToConvertAllTransactionsTo
            );
            amount += transaction.getAmountInConvertedCurrency();
        }

        return amount;
    }

    private void findFromSomethingToTheEndCurrency(Transaction currentTransaction, Float amount,
                                                    String currentTransactionCurrency, String startCurrency, String endCurrency) {
        boolean foundFromSomethingToEndCurrency = false;
        Rate rate;

        for (int i = 0; i < mRates.size(); i++) {
            rate = mRates.get(i);

            // check if the current currency has a rate conversion to our endCurrency
            if (currentTransactionCurrency.equals(rate.getFrom())
                    && endCurrency.equals(rate.getTo())) {
                amount *= Float.parseFloat(rate.getRate());
                currentTransaction.setAmountInConvertedCurrency(amount);
                foundFromSomethingToEndCurrency = true;
                break;
            }
        }

        if (!foundFromSomethingToEndCurrency) {
            findFromSomethingToSomething(
                    currentTransaction, amount,
                    currentTransactionCurrency, startCurrency,
                    endCurrency);
        }
    }

    private void findFromSomethingToSomething(Transaction currentTransaction, Float amount,
                                              String currentTransactionCurrency, String startCurrency, String endCurrency) {
        Rate rate;

        for (int i = 0; i < mRates.size(); i++) {
            rate = mRates.get(i);

            //found from our current currency to something else other than our startCurrency
            if (rate.getFrom().equals(currentTransactionCurrency)
                    && !rate.getTo().equals(startCurrency)) {
                amount *= Float.parseFloat(rate.getRate());
                //check again if this rate currency has a conversion to our FINAL_CURRENCY
                findFromSomethingToTheEndCurrency(currentTransaction, amount, rate.getTo(), startCurrency, endCurrency);
                break;
            }
        }
    }
}