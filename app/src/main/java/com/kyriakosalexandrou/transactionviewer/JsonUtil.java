package com.kyriakosalexandrou.transactionviewer;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kyriakosalexandrou.transactionviewer.models.Rate;
import com.kyriakosalexandrou.transactionviewer.models.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kyriakos on 13/12/2015.
 */
public class JsonUtil {

    public static String loadJSONFromAsset(Context context, String filePath) {
        String json;
        try {
            InputStream is = context.getAssets().open(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static ArrayList<Transaction> createTransactions(Context context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String transactionData = JsonUtil.loadJSONFromAsset(context, "transactions.json");

        Type collectionType = new TypeToken<Collection<Transaction>>() {
        }.getType();
        return gson.fromJson(transactionData, collectionType);
    }

    public static ArrayList<Rate> createRates(Context context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String rateData = JsonUtil.loadJSONFromAsset(context, "rates.json");

        Type collectionType = new TypeToken<Collection<Rate>>() {
        }.getType();
        return gson.fromJson(rateData, collectionType);
    }
}
