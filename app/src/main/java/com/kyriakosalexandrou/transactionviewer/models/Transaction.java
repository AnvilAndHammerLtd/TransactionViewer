package com.kyriakosalexandrou.transactionviewer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by Kyriakos on 13/12/2015.
 */
public class Transaction implements Serializable{

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("currency")
    @Expose
    private String currency;
    private float mAmountInConvertedCurrency;

    public float getAmountInConvertedCurrency() {
        return mAmountInConvertedCurrency;
    }

    public void setAmountInConvertedCurrency(float amount) {
        mAmountInConvertedCurrency = amount;
    }

    /**
     * @return The amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return The sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku The sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}