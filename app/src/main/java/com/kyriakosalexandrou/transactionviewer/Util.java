package com.kyriakosalexandrou.transactionviewer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kyriakos on 13/12/2015.
 */
public class Util {

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
}
