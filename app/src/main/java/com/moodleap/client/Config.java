package com.moodleap.client;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static String baseUrl = null;

    public static String getBaseUrl(Context context) {
        if (baseUrl != null) return baseUrl;

        try {
            InputStream is = context.getAssets().open("config.properties");
            Properties props = new Properties();
            props.load(is);
            baseUrl = props.getProperty("BASE_URL");
        } catch (IOException e) {
            e.printStackTrace();
            baseUrl = "http://fallback.url";
        }

        return baseUrl;
    }
}
