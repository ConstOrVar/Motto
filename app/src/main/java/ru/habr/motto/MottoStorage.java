package ru.habr.motto;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by komar on 11/6/17.
 */

public class MottoStorage {
    private static final String PREFS_NAME = "mottto_prefs";
    private static final String MOTTO_KEY = "motto";

    private final SharedPreferences sharedPreferences;

    public static synchronized MottoStorage getInstance(Context context) {
        return new MottoStorage(context);
    }

    private MottoStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getMotto() {
        return sharedPreferences.getString(MOTTO_KEY,"");
    }

    public void saveMotto(@Nullable String motto) {
        sharedPreferences.edit()
                .putString(MOTTO_KEY, motto)
                .commit();
    }
}
