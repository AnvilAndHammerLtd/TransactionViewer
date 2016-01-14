package com.kyriakosalexandrou.transactionviewer.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kyriakos on 13/12/2015.
 */
public class Product implements Serializable {
//    private static final long serialVersionUID = -7060210544600464481L;

    private String mSKU;
    private ArrayList<Transaction> mTransactions = new ArrayList<>();
    private float mTotalAmount = 0;

    public String getSKU() {
        return mSKU;
    }

    public ArrayList<Transaction> getTransactions() {
        return mTransactions;
    }

    public int getTotalTransactions() {
        return mTransactions.size();
    }

    public float getTotalAmountInGBP() {
        return mTotalAmount;
    }

    public void addToTotalAmount(float add) {
        mTotalAmount += add;
    }

    public Product(String mSKU, Transaction transaction) {
        this.mSKU = mSKU;
        this.mTransactions.add(transaction);
    }

    public void addTransaction(Transaction transaction) {
        this.mTransactions.add(transaction);
    }
}