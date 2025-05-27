package com.example.indiv1;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import java.util.Locale;

public class LocaleHelper {

    private static final String TAG ="LocalHelper";
    public static Context setLocale(Context context, String language) {
        Log.d(TAG, "Setting local to: " + language);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "Using createConfigurationContext");

            return context.createConfigurationContext(config);
        } else {
            Log.d(TAG, "Using updateConfiguration");
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            return context;
        }
    }
}
